# Notice Sorter — iQOO Hackathon (Smart Education)

**Team:** Third Wheelers  
**Track:** Smart Education | **Event:** iQOO Hackathon — Bengaluru  
**Authors:** Aman Jain (`feature/app-ui-calendar`), Prit Thacker (`feature/ocr-llm-pipeline`)

---

## 1. Overview
**Notice Sorter** is a share-target Android app that transforms forwarded college notices (exams, fee circulars, timetable updates, event posters) from WhatsApp into pre-filled calendar events with automated reminders in one tap.

It extends iQOO OriginOS's AI Screen Translation & DocMaster vision from reading text to **acting on what it means**.

---

## 2. Architecture & Shared Contract

```
[WhatsApp/Gallery Share] ──> [Notice Sorter Share Intent]
                                      │
                           [ML Kit OCR - On Device]
                                      │
                           [LLM Extraction API]
                                      │
                           [Interactive Result UI]
                                      │
                     [Native Android Calendar Intent]
```

### Shared Data Contract (`NoticeData`)
```json
{
  "title": "string",
  "date": "YYYY-MM-DD",
  "time": "HH:MM or null",
  "type": "exam | fee | event | circular | other",
  "action_needed": "string, short description",
  "confidence": "high | low"
}
```

---

## 3. Branching Strategy

- `main`: Core skeleton and base repository structure.
- `feature/ocr-llm-pipeline`: Prit's branch — ML Kit OCR, LLM extraction prompt engineering, edge-case date resolution.
- `feature/app-ui-calendar`: Aman's branch — Share intent filter, Jetpack Compose UI, interactive editable card, low-confidence handling, calendar intent integration.
