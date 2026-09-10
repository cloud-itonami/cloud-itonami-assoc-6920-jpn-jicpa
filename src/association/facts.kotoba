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

  ## Dates come off the document's own cover page, not off the page that links it

  倫理規則 and 倫理規則実務ガイダンス第１号 both print their own enactment and
  revision history on page 1, so `:established-date` and `:last-revised-date`
  for those two were read out of the PDF itself (the strictest tier, same as
  sonpo/bankenverband/naic). Where a source states no precise date --
  every `:governance-program` entry below -- the field is ABSENT rather than
  guessed. Absent is not the same as unknown-and-filled-in.

  ## 2026-09-06: the 倫理規則 citation was pointing at a superseded edition

  The entry cited `2-22-0-2-20190618.pdf` with `:last-revised-date
  \"2019-07-22\"`. That was an accurate description of THAT document -- its
  cover page does say 最終改正2019年７月22日 -- but the document had been
  superseded four times over. The current edition's cover
  (`2-22-0-2-20260730.pdf`, same 2-22-0-2 document code) prints the chain:

      改正2019年７月22日 → 改正2022年７月25日 → 改正2024年７月18日
      → 改正2025年７月23日 → 最終改正2026年７月22日

  Three of those four revisions predate this entry's own
  `:retrieved-at \"2026-07-15\"`, so the record was already stale when it was
  written -- this is not drift that accumulated afterwards. A catalog whose
  job is to answer \"what is the spec-basis\" pointing at a seven-year-old
  edition answers with the wrong rules, and does it in the confident shape of
  a correct answer.

  The superseded URL is kept on the entry as `:association-rule/supersedes-url`
  rather than deleted, so the correction stays visible in the data and a later
  pass does not rediscover the 2019 PDF and re-add it as though it were
  current.

  Every URL below was fetched on 2026-09-06 and returned 200 with the expected
  content type. That check is worth doing explicitly: the quality-control
  review STANDARD (品質管理レビュー基準・手続) also returns 200, but it
  redirects to the members-only SSO login, so the bytes behind it are not the
  standard and it is deliberately NOT cited here."
  (:require [kotoba.lang.text :as str]))

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"jicpa"
   [{:association-rule/id "jicpa.ethics-rules"
     :association-rule/title "倫理規則 (Ethics Rules)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://jicpa.or.jp/specialized_field/files/2-22-0-2-20260730.pdf"
     :association-rule/url-provenance :official-association-site
     ;; Superseded edition this entry used to cite. Kept, not deleted.
     :association-rule/supersedes-url "https://jicpa.or.jp/specialized_field/2-22-0-2-20190618.pdf"
     :association-rule/established-date "1966-12-01"
     :association-rule/last-revised-date "2026-07-22"
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:ethics :independence :member-conduct}}
    {:association-rule/id "jicpa.self-regulatory-initiatives"
     :association-rule/title "自主規制の取り組み (Self-Regulatory Initiatives)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/criterion/"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:governance :audit-quality}}
    {:association-rule/id "jicpa.ethics-practice-guidance-1"
     :association-rule/title "倫理規則実務ガイダンス第１号「倫理規則に関するQ&A（実務ガイダンス）」"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :practice-guidance
     :association-rule/url "https://jicpa.or.jp/specialized_field/files/2-22-1-2a-20260430r.pdf"
     :association-rule/url-provenance :official-association-site
     ;; Cover page: 2022年12月15日 / 改正2023年９月７日 / 改正2024年５月23日 /
     ;; 最終改正2026年４月17日. The linking page says the file was posted
     ;; 2026-04-30 and became final at the July general meeting; the
     ;; document's own cover is what is recorded.
     :association-rule/established-date "2022-12-15"
     :association-rule/last-revised-date "2026-04-17"
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:ethics :independence}}
    {:association-rule/id "jicpa.quality-control-review"
     :association-rule/title "品質管理レビュー制度 (Quality Control Review System)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/quality/"
     :association-rule/url-provenance :official-association-site
     ;; The page says 「1999年度から実施しています」 -- a fiscal year, not a
     ;; date, so :established-date is absent rather than invented.
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:governance :audit-quality}}
    {:association-rule/id "jicpa.listed-company-auditor-registration"
     :association-rule/title "上場会社等監査人登録制度 (Listed Company Audit Firm Registration System)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/lcaf/"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:governance :registration}}
    {:association-rule/id "jicpa.case-inspection"
     :association-rule/title "個別事案審査制度 (Individual Case Review System)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/inspection/"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:governance :discipline :member-conduct}}
    {:association-rule/id "jicpa.self-regulation-monitoring"
     :association-rule/title "自主規制のモニタリング (Self-Regulation Monitoring)"
     :association-rule/association "jicpa"
     :association-rule/isic "6920"
     :association-rule/country "JPN"
     :association-rule/kind :governance-program
     :association-rule/url "https://jicpa.or.jp/about/activity/self-regulatory/monitoring/"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-09-06"
     :association-rule/topic #{:governance :oversight}}]})

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

(defn superseded-citations
  "Entries that record the URL they used to cite, with what replaced it.

  A catalog that silently swaps a URL cannot answer \"was this ever wrong\",
  which is the question an auditor actually asks."
  [assoc-slug]
  (into []
        (keep (fn [e]
                (when-let [old (:association-rule/supersedes-url e)]
                  {:association-rule/id (:association-rule/id e)
                   :superseded-url old
                   :current-url (:association-rule/url e)
                   :current-last-revised (:association-rule/last-revised-date e)})))
        (spec-basis assoc-slug)))

(defn citation-hosts
  "Distinct hosts every entry cites, so \"official site only\" is checkable
  rather than asserted in a docstring."
  [assoc-slug]
  (->> (spec-basis assoc-slug)
       (mapcat (juxt :association-rule/url :association-rule/supersedes-url))
       (remove nil?)
       (map #(second (str/split % #"/+" 3)))
       distinct
       sort
       vec))
