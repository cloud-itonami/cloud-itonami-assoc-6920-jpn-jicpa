(ns association.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest jicpa-has-spec-basis
  (let [sb (facts/spec-basis "jicpa")]
    (is (= 2 (count sb)))
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
  (is (= ["jicpa.ethics-rules"]
         (mapv :association-rule/id (facts/by-topic "jicpa" :independence))))
  (is (empty? (facts/by-topic "jicpa" :labor)))
  (is (empty? (facts/by-topic "keidanren" :ethics))))
