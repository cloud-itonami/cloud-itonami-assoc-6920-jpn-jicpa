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
  on the set, which is not stable to rely on."
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [association.facts :as facts]
            [kotoba.compiler.core :as compiler]
            [kotoba.kir :as ir]))

(def ^:private source (slurp "src/association_facts.kotoba"))
(def ^:private kir (:kir (compiler/compile-source source :js-kotoba-v1)))

(defn- call [f & args] (ir/execute kir f (vec args)))
(defn- present [option] (when (second option) (nth option 2)))

(def ^:private fields
  ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
   "established-date" "retrieved-at"])

(def ^:private kw->field
  {"id" :association-rule/id "title" :association-rule/title
   "association" :association-rule/association "isic" :association-rule/isic
   "country" :association-rule/country "kind" :association-rule/kind
   "url" :association-rule/url "url-provenance" :association-rule/url-provenance
   "established-date" :association-rule/established-date
   "retrieved-at" :association-rule/retrieved-at})

(def ^:private entries (facts/spec-basis "jicpa"))

;; The written order of each entry's topic set, which is what the port indexed.
(def ^:private topic-order
  [["ethics" "independence" "member-conduct"]
   ["governance" "audit-quality"]])

(deftest the-fixture-reads-a-real-catalog
  ;; Without this, an empty catalog compares equal to an empty port.
  (is (= 2 (count entries)))
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

(deftest topics-are-complete-and-in-the-order-the-port-chose
  (doseq [[i names] (map-indexed vector topic-order)]
    (testing (str "entry " i)
      (is (= (count names) (call 'topic-count "jicpa" i))
          "one number for both entries is the mistake this invites")
      (is (= (set names)
             (set (map name (:association-rule/topic (nth entries i)))))
          "the written order must name exactly the set the cljc holds")
      (doseq [[t nm] (map-indexed vector names)]
        (is (= nm (present (call 'topic "jicpa" i t))))))))

(deftest by-topic-answers-the-same-entries
  (doseq [names topic-order t names]
    (testing t
      (let [cljc (mapv :association-rule/id (facts/by-topic "jicpa" (keyword t)))]
        (is (= (count cljc) (call 'by-topic-count "jicpa" t)))
        (is (= (first cljc) (present (call 'by-topic-id "jicpa" t 0)))))))
  (is (zero? (call 'by-topic-count "jicpa" "labor")))
  (is (nil? (present (call 'by-topic-id "jicpa" "labor" 0)))))

(deftest an-unknown-association-is-covered-by-nothing
  (doseq [other ["keidanren" "zzz" ""]]
    (is (false? (call 'association-covered? other)))
    (is (zero? (call 'entry-count other)))
    (is (nil? (present (call 'entry-field other 0 "id"))))
    (is (nil? (present (call 'coverage-note other))))
    (is (nil? (facts/spec-basis other)) "and the cljc agrees")))

(deftest the-module-compiles-for-every-target-it-claims
  (doseq [target [:js-kotoba-v1 :wasm32-kotoba-v1 :x86_64-kotoba-v1 :aarch64-kotoba-v1]]
    (testing (name target)
      (is (some? (compiler/compile-source source target {}))))))
