package com.collegeos.feature.noticesorter.ui.components

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.collegeos.feature.noticesorter.model.NoticeData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarLauncher {

    /**
     * Launches Android's native Calendar event creation screen (ACTION_INSERT)
     * pre-populated with extracted title, description, and event date/time.
     */
    fun launchCalendarEvent(context: Context, notice: NoticeData): Boolean {
        return try {
            val startTimeMillis = parseDateTimeToMillis(notice.date, notice.time)
            val endTimeMillis = startTimeMillis + (60 * 60 * 1000) // 1 hour duration default

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, notice.title)
                putExtra(
                    CalendarContract.Events.DESCRIPTION,
                    "Action Required: ${notice.actionNeeded}\n\nNotice Type: ${notice.type.uppercase()}"
                )
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
                putExtra(CalendarContract.Events.EVENT_LOCATION, "College Campus")
                putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                
                // Set default 24-hour (1440 min) reminder preference
                putExtra("hasAlarm", 1)
            }

            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun parseDateTimeToMillis(dateStr: String?, timeStr: String?): Long {
        val calendar = Calendar.getInstance()
        try {
            if (!dateStr.isNullOrBlank()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.parse(dateStr)
                if (date != null) {
                    calendar.time = date
                }
            }

            if (!timeStr.isNullOrBlank()) {
                val timeParts = timeStr.split(":")
                if (timeParts.size >= 2) {
                    val hour = timeParts[0].toIntOrNull() ?: 9
                    val minute = timeParts[1].toIntOrNull() ?: 0
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                }
            } else {
                // Default to 9:00 AM if time not specified
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
            }
        } catch (e: Exception) {
            // Fallback: 1 day from now
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }
}
