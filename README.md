# cloud-itonami-assoc-6920-jpn-jicpa

Industry self-regulatory rule catalog for the **Japanese Institute of
Certified Public Accountants** (日本公認会計士協会 / JICPA) — a 7th
industry-association-level source, and the FIRST aligned to ISIC 6920
(accounting/tax/audit), alongside
[`cloud-itonami-assoc-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo),
[`cloud-itonami-assoc-6512-jpn-sonpo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-jpn-sonpo),
[`cloud-itonami-assoc-6612-jpn-jsda`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-jpn-jsda),
[`cloud-itonami-assoc-6419-deu-bankenverband`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-deu-bankenverband),
[`cloud-itonami-assoc-6612-usa-finra`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-usa-finra),
and
[`cloud-itonami-assoc-6512-usa-naic`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-usa-naic).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on JICPA's behalf.

Coverage is reported honestly (see `association.facts/coverage`): an
entry not in `catalog` has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association/facts.cljc` — the catalog, source of truth.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Dates come off each document's **own cover page**, not off the page that
links it. 倫理規則 (Ethics Rules) is enacted 1966-12-01 and most recently
revised **2026-07-22**; 倫理規則実務ガイダンス第１号 is enacted 2022-12-15
and most recently revised 2026-04-17. Where a source prints no precise
date — every `:governance-program` entry — the field is **absent** rather
than guessed. Absent is not the same as unknown-and-filled-in.

Every URL was fetched on 2026-09-06 and returned 200 with the expected
content type. That check earns its keep: 品質管理レビュー基準・手続 also
returns 200, but redirects to the members-only SSO login, so the bytes
behind it are not the standard — it is deliberately **not** cited here.

### 2026-09-06: the 倫理規則 citation was pointing at a superseded edition

The entry cited `2-22-0-2-20190618.pdf` with `last-revised-date
2019-07-22`. That accurately described *that document* — its cover does
say 最終改正2019年７月22日 — but the document had since been superseded four
times. The current edition (`2-22-0-2-20260730.pdf`, same `2-22-0-2`
document code) prints the chain: 2019-07-22 → 2022-07-25 → 2024-07-18 →
2025-07-23 → **2026-07-22**. Three of those four revisions predate the
entry's own `retrieved-at 2026-07-15`, so the record was already stale
when it was written; this is not drift that accumulated afterwards.

The superseded URL is retained on the entry as `supersedes-url` rather
than deleted, so the correction stays visible in the data and a later
pass does not rediscover the 2019 PDF and re-add it as current.

**Why the gate did not catch it.** `last-revised-date` was in the cljc
from the first commit and was never carried into the Kotoba port, so it
was absent from the parity test's compared-field list — the one field
that says *which edition* an entry describes was the field nobody
compared. Measured on the pre-change tree: mutating that date to
`1900-01-01` left all 10 tests green, while mutating the URL turned them
red. Both it and `supersedes-url` are compared now, and a test asserts
that the compared-field list covers every field the catalog actually
uses.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Rule text itself
remains JICPA's; this repo stores only citation metadata (id/title/url/
dates), not full rule text.
