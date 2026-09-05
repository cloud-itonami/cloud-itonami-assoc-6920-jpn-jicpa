(ns association.facts-test
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest jicpa-has-spec-basis
  (let [sb (facts/spec-basis "jicpa")]
    (is (= 7 (count sb)))
    (is (every? #(str/starts-with? (:association-rule/url %) "https://jicpa.or.jp/") sb))
    (is (every? #(= "6920" (:association-rule/isic %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "keidanren")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["jicpa" "keidanren"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["keidanren"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= ["jicpa.ethics-rules" "jicpa.ethics-practice-guidance-1"]
         (mapv :association-rule/id (facts/by-topic "jicpa" :independence))))
  (is (= ["jicpa.case-inspection"]
         (mapv :association-rule/id (facts/by-topic "jicpa" :discipline))))
  (is (empty? (facts/by-topic "jicpa" :labor)))
  (is (empty? (facts/by-topic "keidanren" :ethics))))

(deftest every-entry-is-uniquely-identified
  ;; :association-rule/id is :db.unique/identity in the schema, so a duplicate
  ;; id does not add an entry -- it silently overwrites one on transact.
  (let [ids (mapv :association-rule/id (facts/spec-basis "jicpa"))]
    (is (= (count ids) (count (distinct ids))))
    (is (every? #(str/starts-with? % "jicpa.") ids))))

(deftest no-entry-carries-an-empty-or-blank-required-field
  ;; A blank string is not a missing field: it reads as present everywhere
  ;; downstream and answers questions with "".
  (doseq [e (facts/spec-basis "jicpa")
          k [:association-rule/id :association-rule/title :association-rule/url
             :association-rule/kind :association-rule/url-provenance
             :association-rule/retrieved-at]]
    (let [v (get e k)]
      (is (some? v) (str (:association-rule/id e) " / " k))
      (is (or (keyword? v) (not (str/blank? v)))
          (str (:association-rule/id e) " / " k)))))

(deftest optional-dates-are-absent-rather-than-blank
  ;; Every governance-program entry below cites a page that prints no
  ;; enactment date. The catalog records that by leaving the key out, not by
  ;; storing "" or "unknown", which would read as a date to anything that
  ;; checks for presence.
  (doseq [e (facts/spec-basis "jicpa")
          k [:association-rule/established-date :association-rule/last-revised-date]]
    (when (contains? e k)
      (is (re-matches #"\d{4}-\d{2}-\d{2}" (get e k))
          (str (:association-rule/id e) " / " k)))))

(deftest a-corrected-citation-keeps-the-url-it-replaced
  ;; The 倫理規則 entry cited 2-22-0-2-20190618.pdf, whose own cover page says
  ;; 最終改正2019年７月22日, while that document had been revised four more
  ;; times (2022, 2024, 2025, 2026). Three of those predate the entry's own
  ;; retrieved-at, so the record was stale when written, not after.
  ;;
  ;; Dropping the old URL would make the correction invisible and let a later
  ;; pass rediscover the 2019 PDF as though it were current.
  (let [[s :as all] (facts/superseded-citations "jicpa")]
    (is (= 1 (count all)))
    (is (= "jicpa.ethics-rules" (:association-rule/id s)))
    (is (= "https://jicpa.or.jp/specialized_field/2-22-0-2-20190618.pdf"
           (:superseded-url s)))
    (is (= "https://jicpa.or.jp/specialized_field/files/2-22-0-2-20260730.pdf"
           (:current-url s)))
    (is (= "2026-07-22" (:current-last-revised s))
        "the cover page of the current edition prints 最終改正2026年７月22日")
    (is (not= (:superseded-url s) (:current-url s)))))

(deftest every-citation-is-on-the-association-s-own-domain
  ;; ":official-association-site" is a claim; this is the check. Superseded
  ;; URLs are included -- a correction that points off-domain is still wrong.
  (is (= ["jicpa.or.jp"] (facts/citation-hosts "jicpa")))
  (is (every? #(= :official-association-site (:association-rule/url-provenance %))
              (facts/spec-basis "jicpa"))))

(deftest the-tx-data-file-says-what-the-catalog-says
  ;; `data/datascript-tx.edn` is the artifact other repos query -- it is what
  ;; `scripts/compliance-fact-query.cljs` loads, and it is a SEPARATE hand-kept
  ;; file from the catalog it claims to be derived from. Nothing compared them
  ;; until now, so the two could disagree and both look fine in isolation: the
  ;; cljc would answer one URL and every consumer would read another.
  (let [tx (edn/read-string (slurp "data/datascript-tx.edn"))
        norm #(update % :association-rule/topic set)
        from-catalog (mapv norm (facts/spec-basis "jicpa"))
        from-file (mapv norm tx)]
    (is (= (count from-catalog) (count from-file)))
    (is (= (mapv :association-rule/id from-catalog)
           (mapv :association-rule/id from-file))
        "same entries, same order")
    (doseq [[c f] (map vector from-catalog from-file)]
      (is (= c f) (str "tx-data diverges from the catalog at "
                       (:association-rule/id c))))))
