(ns association-facts-kotoba-parity-test
  "The JICPA catalog in .cljc and in .kotoba, field by field.

  The cljc holds the catalog as data -- a vector of maps read from the same
  `data/datascript-tx.edn` the Kotoba module transliterates. So this is not two
  implementations of a rule; it is one body of facts written twice, and the
  risk is transcription: a wrong URL, a dropped field, a topic that lost its
  entry.

  Every field of every entry is compared, plus the counts and the topic
  membership, because a catalog is exactly the shape where checking a sample
  checks the entries someone already looked at.

  ## The one thing that is a decision, not a transcription

  `:association-rule/topic` is a SET. A set has no order and `topic` is indexed
  by position, so the port chose the order the source file writes. The
  assertion below compares against that written order rather than against `seq`
  on the set, which is not stable to rely on.

  ## What this test could not see before 2026-09-06

  `last-revised-date` was in the cljc from the beginning and absent from both
  the port and `fields` here -- so the single field that says WHICH EDITION an
  entry describes was the field the parity gate did not compare. A citation
  could point at a seven-year-old edition and every assertion in this file
  still passed. It is in `fields` now, along with the new `supersedes-url`.

  Two derived answers are compared against the cljc rather than against
  literals, so they cannot drift as entries are added: `by-topic-count` against
  `(count (facts/by-topic …))`, and `by-topic-id` at EVERY index against that
  same vector. The port used to answer only index 0, which was
  indistinguishable from correct while every topic had exactly one entry."
  (:require [clojure.test :refer [deftest is testing]]
            [association.facts :as facts]
            [kotoba.compiler.core :as compiler]
            [kotoba.kir :as ir]))

(def ^:private source (slurp "src/association_facts.kotoba"))
(def ^:private kir (:kir (compiler/compile-source source :js-kotoba-v1)))

(defn- call [f & args] (ir/execute kir f (vec args)))
(defn- present [option] (when (second option) (nth option 2)))

(def ^:private fields
  ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
   "supersedes-url" "established-date" "last-revised-date" "retrieved-at"])

(def ^:private kw->field
  {"id" :association-rule/id "title" :association-rule/title
   "association" :association-rule/association "isic" :association-rule/isic
   "country" :association-rule/country "kind" :association-rule/kind
   "url" :association-rule/url "url-provenance" :association-rule/url-provenance
   "supersedes-url" :association-rule/supersedes-url
   "established-date" :association-rule/established-date
   "last-revised-date" :association-rule/last-revised-date
   "retrieved-at" :association-rule/retrieved-at})

(def ^:private entries (facts/spec-basis "jicpa"))

;; The written order of each entry's topic set, which is what the port indexed.
(def ^:private topic-order
  [["ethics" "independence" "member-conduct"]
   ["governance" "audit-quality"]
   ["ethics" "independence"]
   ["governance" "audit-quality"]
   ["governance" "registration"]
   ["governance" "discipline" "member-conduct"]
   ["governance" "oversight"]])

(deftest the-fixture-reads-a-real-catalog
  ;; Without this, an empty catalog compares equal to an empty port.
  (is (= 7 (count entries)))
  (is (= (count entries) (count topic-order))
      "a new entry with no topic-order row would otherwise go uncompared")
  (is (every? #(= "6920" (:association-rule/isic %)) entries)))

(deftest every-field-of-every-entry-is-transcribed
  (is (= (count entries) (call 'entry-count "jicpa")))
  (doseq [[i entry] (map-indexed vector entries)]
    (doseq [f fields]
      (testing (str "entry " i " / " f)
        (let [expected (get entry (kw->field f))
              expected (cond (keyword? expected) (name expected)
                             (nil? expected) nil
                             :else expected)]
          (is (= expected (present (call 'entry-field "jicpa" i f)))))))))

(deftest the-compared-field-set-covers-every-field-the-catalog-uses
  ;; The failure this test exists for: a field added to the cljc and to the
  ;; port, but not to `fields` above, is transcribed and never compared. That
  ;; is how `last-revised-date` went unchecked from the first commit until
  ;; 2026-09-06.
  ;; `fields` is what the comparison loop iterates, so `fields` is what this
  ;; has to inspect. Reading `kw->field` instead looks equivalent and is not:
  ;; a field dropped from `fields` alone stops being compared while its
  ;; mapping stays behind, and this guard reports clean. Caught by mutation --
  ;; the first version of this test passed with `last-revised-date` removed
  ;; from `fields`, which is the exact failure it was written to prevent.
  (let [used (into #{} (mapcat keys) entries)
        compared (into #{} (map kw->field) fields)
        uncompared (remove compared used)]
    (is (empty? (remove #{:association-rule/topic} uncompared))
        (str "catalog fields nobody compares: " (pr-str uncompared)))
    (is (= (set fields) (set (keys kw->field)))
        "a name in one structure and not the other is a field half-wired")))

(deftest topics-are-complete-and-in-the-order-the-port-chose
  (doseq [[i names] (map-indexed vector topic-order)]
    (testing (str "entry " i)
      (is (= (count names) (call 'topic-count "jicpa" i))
          "one number for every entry is the mistake this invites")
      (is (= (set names)
             (set (map name (:association-rule/topic (nth entries i)))))
          "the written order must name exactly the set the cljc holds")
      (doseq [[t nm] (map-indexed vector names)]
        (is (= nm (present (call 'topic "jicpa" i t)))))
      (is (nil? (present (call 'topic "jicpa" i (count names))))
          "one past the end is absent, not the last topic again"))))

(deftest by-topic-answers-the-same-entries-at-every-index
  (doseq [t (distinct (apply concat topic-order))]
    (testing t
      (let [cljc (mapv :association-rule/id (facts/by-topic "jicpa" (keyword t)))]
        (is (seq cljc) "topic-order named a topic the cljc does not carry")
        (is (= (count cljc) (call 'by-topic-count "jicpa" t)))
        ;; Every index, not just the first: answering only index 0 is
        ;; indistinguishable from correct while each topic has one entry.
        (doseq [[i id] (map-indexed vector cljc)]
          (is (= id (present (call 'by-topic-id "jicpa" t i)))
              (str "index " i)))
        (is (nil? (present (call 'by-topic-id "jicpa" t (count cljc))))
            "one past the end is absent"))))
  (is (zero? (call 'by-topic-count "jicpa" "labor")))
  (is (nil? (present (call 'by-topic-id "jicpa" "labor" 0)))))

(deftest an-unknown-association-is-covered-by-nothing
  (doseq [other ["keidanren" "zzz" ""]]
    (is (false? (call 'association-covered? other)))
    (is (zero? (call 'entry-count other)))
    (is (nil? (present (call 'entry-field other 0 "id"))))
    (is (nil? (present (call 'coverage-note other))))
    (is (nil? (present (call 'by-topic-id other "ethics" 0)))
        "a topic that exists must still not answer for an association that does not")
    (is (nil? (facts/spec-basis other)) "and the cljc agrees")))

(deftest an-index-past-the-catalog-is-absent
  (doseq [i [7 8 99]]
    (is (nil? (present (call 'entry-field "jicpa" i "id"))))
    (is (zero? (call 'topic-count "jicpa" i)))))

(deftest the-module-compiles-for-every-target-it-claims
  (doseq [target [:js-kotoba-v1 :wasm32-kotoba-v1 :x86_64-kotoba-v1 :aarch64-kotoba-v1]]
    (testing (name target)
      (is (some? (compiler/compile-source source target {}))))))
