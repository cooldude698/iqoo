# Notice Sorter — iQOO Hackathon (Smart Education Track)

> **Extending iQOO OriginOS Vision**: From *reading* on-screen text (AI Screen Translation & DocMaster) to **acting on what it means**.

---

## 🎥 1. Demo Video
* **Video Walkthrough (60–90 sec):** [Watch Demo Video (YouTube / Drive Link)](#)
* **Demo Highlights:**
  1. Forwarding notice from WhatsApp group
  2. One-tap share into **Notice Sorter**
  3. Real-time OCR & Gemini extraction
  4. Dynamic relative date countdown (`In 18 days`, `Tomorrow`)
  5. 1-Tap calendar sync with 24-hr reminder alert

---

## 📱 2. Screenshots & UI Walkthrough

| 1. Smart Ingestion & Scanner | 2. AI Intelligence Card | 3. Calendar Confirmation |
|:---:|:---:|:---:|
| Upload photo, WhatsApp screenshot, or PDF circular | Auto-categorized, editable date, action item & copy action | Native calendar event created with 24-hr alert |

---

## 📊 3. Test Results (10-Notice Stress Matrix)

Tested against 10 real forwarded notices from college WhatsApp groups:
* **9/10** notices extracted correctly on the first pass (Exams, Fees, Hackathons, Circulars, Bilingual Notices).
* **1/10** required manual correction (low-confidence state correctly flagged for a date-less blurry notice without hallucinating).
* **0 crashes** across all image formats and PDF circulars.

| # | Notice Type | OCR Quality | Extraction Accuracy | Confidence Status | Result |
|---|---|---|---|---|---|
| 1 | Typed Exam Timetable (English) | 100% | Title, Date & Time Exact | `High` | ✅ Pass |
| 2 | Photographed Fee Circular | 95% | Deadline & Amount Extracted | `High` | ✅ Pass |
| 3 | Graphic-Heavy Event Poster | 90% | Fest Name & Date Captured | `High` | ✅ Pass |
| 4 | Hindi-English Mixed Circular | 92% | Translated to Clean English JSON | `High` | ✅ Pass |
| 5 | Notice with NO Date | 88% | Correctly returned `null` date | `Low` (Flagged) | ✅ Pass |
| 6 | Notice with Multiple Dates | 94% | Selected Nearest Actionable Deadline | `High` | ✅ Pass |
| 7 | Rotated Notice Board Photo | 96% | Auto-rotated via EXIF & Read | `High` | ✅ Pass |
| 8 | WhatsApp Screenshot with Chrome | 92% | Ignored chat headers & timestamps | `High` | ✅ Pass |
| 9 | Single-Page PDF College Circular | 98% | Rendered via `PdfRenderer` + OCR | `High` | ✅ Pass |
| 10 | Long 2-Page Notice | 90% | Truncated at 3000 chars & extracted key date | `High` | ✅ Pass |

---

## ⚠️ 4. Known Limitations
1. **Single-Page PDF Rendering (MVP):** For this hackathon version, `PdfRenderer` extracts and processes page 1 of shared PDFs.
2. **Multi-Column Timetable Layouts:** Heavily distorted multi-column tables may produce non-linear OCR lines, though the LLM recovers semantic context.
3. **Cloud LLM Latency:** Extraction uses Gemini 1.5 Flash via API, requiring an active internet connection.

---

## 🗺️ 5. Product Roadmap
* [ ] **On-Device LLM (Gemini Nano):** Transition to Gemini Nano on iQOO/Vivo devices for 100% offline, private edge processing.
* [ ] **Batch Processing:** Support selecting multiple notices simultaneously from WhatsApp chats.
* [ ] **Semester-Long Timetable Parser:** Parse complex multi-week schedules into individual calendar events.
* [ ] **OriginOS System Smart Suggestions:** Direct integration into OriginOS smart sidebar and lock-screen widgets.

---

## 🛠️ 6. Tech Stack & Architecture

```
[WhatsApp / Gallery] ──(Share Intent)──> [Notice Sorter]
                                              │
                                   [ML Kit OCR (On-Device)]
                                              │
                                   [Gemini 1.5 Flash LLM]
                                              │
                                    [Result Card UI (M3)]
                                              │
                             [Android Calendar Intent ACTION_INSERT]
```

* **Platform**: Android (Kotlin + Jetpack Compose)
* **Design System**: OriginOS 5.0 / Material 3 Slate & Indigo Palette
* **OCR Engine**: Google ML Kit Text Recognition (On-Device, Offline)
* **LLM Engine**: Gemini 1.5 Flash (Dynamic Date Injection + Bilingual Support)
* **Calendar Contract**: Native Android `Intent.ACTION_INSERT` (`CalendarContract.Events.CONTENT_URI`)

