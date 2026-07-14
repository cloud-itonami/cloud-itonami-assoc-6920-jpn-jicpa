(ns association.facts
  "Industry self-regulatory rule catalog for the Japanese Institute of
  Certified Public Accountants (日本公認会計士協会 / JICPA) -- a 7th
  industry-association-level source (see cloud-itonami-assoc-6419-jpn-zenginkyo,
  -6512-jpn-sonpo, -6612-jpn-jsda, -6419-deu-bankenverband,
  -6612-usa-finra, -6512-usa-naic for the first six) per ADR-2607141700
  (cloud-itonami-compliance-fact-federation). The FIRST entry aligned to
  ISIC 6920 (accounting/tax/audit) -- a new industry code for this
  family, not yet paired cross-country. Every entry cites an OFFICIAL
  jicpa.or.jp URL -- never fabricated. A rule not in this table has NO
  spec-basis, full stop; extend `catalog`, do not invent an id/url.

  倫理規則 (Ethics Rules) was verified by directly reading the source PDF
  cover page text via the Read tool (same strictest-tier verification as
  sonpo/bankenverband/naic) -- the enactment/revision date history is
  printed on the document's own cover page. The self-regulatory
  overview page was directly WebFetch-verified.")

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"jicpa"
   [{:association-rule/id "jicpa.ethics-rules"
     :association-rule/title "倫理規則 (Ethics Rules)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://jicpa.or.jp/specialized_field/2-22-0-2-20190618.pdf"
     :association-rule/url-provenance :official-association-site
     :association-rule/established-date "1966-12-01"
     :association-rule/last-revised-date "2019-07-22"
     :association-rule/retrieved-at "2026-07-15"
     :association-rule/topic #{:ethics :independence :member-conduct}}
    {:association-rule/id "jicpa.self-regulatory-initiatives"
     :association-rule/title "自主規制の取り組み (Self-Regulatory Initiatives)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/criterion/"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-07-15"
     :association-rule/topic #{:governance :audit-quality}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6920-jpn-jicpa Wave 0 (ADR-2607141700): "
                 (count (get catalog "jicpa")) " jicpa entries seeded with an "
                 "official jicpa.or.jp citation. Extend "
                 "`association.facts/catalog`, never fabricate a rule id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))
