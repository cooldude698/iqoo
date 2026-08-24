package com.iqoo.noticesorter.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.noticesorter.model.NoticeData
import com.iqoo.noticesorter.model.NoticeType
import com.iqoo.noticesorter.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun NoticeCard(
    notice: NoticeData,
    onNoticeUpdated: (NoticeData) -> Unit,
    onAddToCalendar: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showEditTitle by remember { mutableStateOf(false) }
    var showEditDate by remember { mutableStateOf(false) }
    var showEditTime by remember { mutableStateOf(false) }
    var showEditType by remember { mutableStateOf(false) }
    var showEditAction by remember { mutableStateOf(false) }

    val noticeType = notice.noticeTypeEnum

    // Relative date calculation helper (e.g. "In 3 days", "Tomorrow")
    val relativeDateBadge = remember(notice.date) {
        calculateRelativeDate(notice.date)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = Color(0x1F0F172A),
                ambientColor = Color(0x0A0F172A)
            ),
        shape = RoundedCornerShape(26.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {

            // 1. Category Pill & AI Confidence Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip (Tap to change)
                Surface(
                    color = noticeType.containerColor,
                    contentColor = noticeType.contentColor,
                    shape = CircleShape,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            showEditType = true
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (noticeType) {
                            NoticeType.EXAM -> Icons.AutoMirrored.Filled.Assignment
                            NoticeType.FEE -> Icons.Default.AccountBalanceWallet
                            NoticeType.EVENT -> Icons.Default.Celebration
                            NoticeType.CIRCULAR -> Icons.Default.Description
                            NoticeType.OTHER -> Icons.Default.Info
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = noticeType.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Change category",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Confidence Status Badge
                if (notice.isLowConfidence) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        contentColor = ExamAmber,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Needs Review",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Surface(
                        color = Color(0xFFECFDF5),
                        contentColor = FeeEmerald,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Verified",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Notice Title (Interactive Tap-to-Edit)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        showEditTitle = true
                    }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.title.ifBlank { "Untitled Notice" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    lineHeight = 28.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfaceCardSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Title",
                        tint = TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Date & Time Widget (Split Modern Cards)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceCardSecondary)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date Column
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            showEditDate = true
                        }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = BrandIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "EVENT DATE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                            if (relativeDateBadge != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFFE0E7FF),
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = relativeDateBadge,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandIndigo,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (!notice.date.isNullOrBlank()) notice.date else "Set Date ➔",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (!notice.date.isNullOrBlank()) TextPrimary else ExamAmber
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(BorderSubtle)
                )

                // Time Column
                Row(
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            showEditTime = true
                        }
                        .padding(start = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = FeeEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TIME",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notice.time ?: "All Day",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Low Confidence Warning Alert
            if (notice.isLowConfidence) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFFFFFBEB),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = null,
                            tint = ExamAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "No exact date found in notice image. Tap the date box above to set the event day.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Action Required Hero Card
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = BrandIndigo,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ACTION REQUIRED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandIndigo,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Copy Action Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Notice Action", notice.actionNeeded)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Action copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Copy",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                showEditAction = true
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notice.actionNeeded.ifBlank { "No action specified. Tap to add details." },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = TextPrimary,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Action",
                            tint = TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Primary Glowing "Add to Calendar" CTA
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(ActionButtonGradient)
                    .clickable {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onAddToCalendar()
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EventAvailable,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Add to Phone Calendar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Syncs event & enables 24-hr reminder alert",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE0E7FF),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }

    // Dialog Renderers
    if (showEditTitle) {
        EditTitleDialog(
            currentTitle = notice.title,
            onDismiss = { showEditTitle = false },
            onConfirm = { updatedTitle -> onNoticeUpdated(notice.copy(title = updatedTitle)) }
        )
    }
    if (showEditAction) {
        EditActionDialog(
            currentAction = notice.actionNeeded,
            onDismiss = { showEditAction = false },
            onConfirm = { updatedAction -> onNoticeUpdated(notice.copy(actionNeeded = updatedAction)) }
        )
    }
    if (showEditType) {
        EditTypeDialog(
            currentType = notice.type,
            onDismiss = { showEditType = false },
            onConfirm = { updatedType -> onNoticeUpdated(notice.copy(type = updatedType)) }
        )
    }
    if (showEditDate) {
        EditDateDialog(
            currentDate = notice.date ?: "",
            onDismiss = { showEditDate = false },
            onConfirm = { updatedDate ->
                onNoticeUpdated(
                    notice.copy(
                        date = updatedDate,
                        confidence = if (updatedDate.isNotBlank()) "high" else notice.confidence
                    )
                )
            }
        )
    }
    if (showEditTime) {
        EditTimeDialog(
            currentTime = notice.time,
            onDismiss = { showEditTime = false },
            onConfirm = { updatedTime -> onNoticeUpdated(notice.copy(time = updatedTime)) }
        )
    }
}

/**
 * Calculates human-readable relative date (e.g. "Today", "Tomorrow", "In 5 days")
 */
private fun calculateRelativeDate(dateStr: String?): String? {
    if (dateStr.isNullOrBlank()) return null
    return try {
        val targetDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(today, targetDate)
        when {
            daysBetween == 0L -> "Today"
            daysBetween == 1L -> "Tomorrow"
            daysBetween > 1L -> "In $daysBetween days"
            daysBetween == -1L -> "Yesterday"
            daysBetween < -1L -> "${-daysBetween}d ago"
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

