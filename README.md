# Notice Sorter — iQOO Hackathon (Smart Education Track)

> **Extending iQOO OriginOS Vision**: From *reading* on-screen text (AI Screen Translation & DocMaster) to **acting on what it means**.

---

## 1. The Product
Notice Sorter solves a real everyday problem for Indian students: critical exam dates, fee deadlines, timetable changes, and event notices arrive as forwarded images or PDFs in crowded WhatsApp groups. Information gets buried and deadlines get missed.

With Notice Sorter, a student shares any notice image or PDF directly into the app from WhatsApp or Files. The app:
1. Extracts the text (on-device ML Kit OCR).
2. Understands key details (LLM extraction: Title, Date, Time, Notice Type, Action Item).
3. Displays a clean, editable card with low-confidence handling.
4. Adds the event to the phone calendar with a 24-hour reminder in **one tap**.

---

## 2. Team & Branch Architecture

```
main
 ├── feature/ocr-llm-pipeline     (Prit — OCR + LLM Pipeline)
 └── feature/app-ui-calendar      (Aman — UI, Share Intent, Calendar & Pitch)
```

- **Aman (`feature/app-ui-calendar`)**: Share-intent receiver, Jetpack Compose UI, interactive card editing, low-confidence state, native calendar integration, visual design system, and demo video script.
- **Prit (`feature/ocr-llm-pipeline`)**: ML Kit OCR integration, LLM prompt engineering, structured JSON parsing, and `processNotice(imageUri)` function.

---

## 3. Shared Data Contract (`NoticeData`)

Both branches communicate strictly via this contract:

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

## 4. Tech Stack

- **Platform**: Android (Kotlin + Jetpack Compose)
- **UI Architecture**: Material 3 + Custom Positive Color Palette (`#5E7892`, `#A7B7C6`, `#F3EFDF`, `#BDCFAA`, `#8E9E83`)
- **OCR Engine**: Google ML Kit Text Recognition (On-device, offline, zero latency)
- **Extraction API**: Gemini / OpenAI / Claude API
- **Calendar Integration**: Native Android `Intent.ACTION_INSERT` (`CalendarContract.Events.CONTENT_URI`) — zero special permissions required

---

## 5. Development Roadmap & Task Breakdown

### Task 1: Share-Intent Handling
- Registered `ACTION_SEND` intent filter in `AndroidManifest.xml` for `image/*` and `application/pdf`.
- Extracted shared URI via `Intent.EXTRA_STREAM` in `MainActivity.kt`.
- Built loading state screen ("Reading your notice...").

### Task 2: Result Card UI & Low-Confidence Handling
- Interactive Material 3 card showing Title, Date/Time, Action Required, and Notice Type badges.
- Tappable/editable fields for user corrections.
- Explicit low-confidence state warning banner when date detection is unclear or missing.

### Task 3: Calendar Integration
- Wired "Add to Phone Calendar" button using `Intent.ACTION_INSERT`.
- Pre-fills event title, description (action item), start time, and 24-hour reminder.
- Renders post-addition confirmation screen.

### Task 4: Visual Polish & Palette Design System
- Custom theme matching the hackathon palette (`#5E7892`, `#A7B7C6`, `#F3EFDF`, `#BDCFAA`, `#8E9E83`).
- Pill containers, smooth state transitions, and responsive typography hierarchy.

### Task 5: Pipeline Integration
- Interface `NoticeProcessor` allows seamless swap between `MockNoticeProcessor` and Prit's real `processNotice(imageUri)`.

### Task 6: Pitch & Demo Script (60–90 sec)
1. Open WhatsApp & view forwarded college notice (5s).
2. Tap Share -> Select **Notice Sorter** (5s).
3. Loading screen -> Result card auto-populates (10s).
4. Tap date to demonstrate editing capability (10s).
5. Tap "Add to Calendar" -> Show real calendar entry created with reminder (10s).
6. Closing line: *"Notice Sorter extends OriginOS from reading text to acting on what it means."*
