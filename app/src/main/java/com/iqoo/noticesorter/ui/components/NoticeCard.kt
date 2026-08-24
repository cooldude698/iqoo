package com.iqoo.noticesorter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.noticesorter.model.NoticeData
import com.iqoo.noticesorter.model.NoticeType
import com.iqoo.noticesorter.ui.theme.*

@Composable
fun NoticeCard(
    notice: NoticeData,
    onNoticeUpdated: (NoticeData) -> Unit,
    onAddToCalendar: () -> Unit
) {
    var showEditTitle by remember { mutableStateOf(false) }
    var showEditDate by remember { mutableStateOf(false) }
    var showEditTime by remember { mutableStateOf(false) }
    var showEditType by remember { mutableStateOf(false) }
    var showEditAction by remember { mutableStateOf(false) }

    val noticeType = notice.noticeTypeEnum

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp), // Smooth positive pill shape
        colors = CardDefaults.cardColors(containerColor = PaletteCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {

            // Header Tag Row (Notice Category Badge & iQOO Phone-First Badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = noticeType.containerColor,
                    contentColor = noticeType.contentColor,
                    shape = CircleShape,
                    modifier = Modifier.clickable { showEditType = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (noticeType) {
                            NoticeType.EXAM -> Icons.Default.Assignment
                            NoticeType.FEE -> Icons.Default.AccountBalanceWallet
                            NoticeType.EVENT -> Icons.Default.Event
                            NoticeType.CIRCULAR -> Icons.Default.Description
                            NoticeType.OTHER -> Icons.Default.Info
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = noticeType.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Category",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                if (notice.isLowConfidence) {
                    Surface(
                        color = Color(0xFFFFF4E5),
                        contentColor = Color(0xFFB76E00),
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Needs Date",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Surface(
                        color = Color(0xFFF0F4F2),
                        contentColor = PaletteMossGreen,
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
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

            Spacer(modifier = Modifier.height(18.dp))

            // Notice Title (Large & Editable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showEditTitle = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.title.ifBlank { "Untitled Notice" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PaletteDarkText,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Title",
                    tint = PaletteSoftSteel,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Time Block (Pill design with Palette background)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F8FA), shape = RoundedCornerShape(18.dp))
                    .border(1.dp, PaletteCardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Row(
                    modifier = Modifier
                        .clickable { showEditDate = true }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8EEF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = PaletteSlateBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "EVENT DATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = PaletteSubtext
                        )
                        Text(
                            text = if (notice.date.isNotBlank()) notice.date else "Tap to set date",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (notice.date.isNotBlank()) PaletteDarkText else Color(0xFFB76E00)
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp),
                    color = PaletteCardBorder
                )

                // Time
                Row(
                    modifier = Modifier
                        .clickable { showEditTime = true }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFF4EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = PaletteMossGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TIME",
                            style = MaterialTheme.typography.labelSmall,
                            color = PaletteSubtext
                        )
                        Text(
                            text = notice.time ?: "All Day",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = PaletteDarkText
                        )
                    }
                }
            }

            // Low Confidence Alert Banner
            if (notice.isLowConfidence) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF4E5), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFB76E00),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Unclear date in notice image. Tap the date block above to fix.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB76E00)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Required Block
            Text(
                text = "ACTION REQUIRED",
                style = MaterialTheme.typography.labelSmall,
                color = PaletteSubtext
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showEditAction = true }
                    .background(Color(0xFFFAFBFD))
                    .border(1.dp, PaletteCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.action_needed.ifBlank { "No action specified. Tap to add details." },
                    style = MaterialTheme.typography.bodyMedium,
                    color = PaletteDarkText,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Action",
                    tint = PaletteSoftSteel,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Primary Add to Calendar CTA with Positive Palette Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(CircleShape)
                    .background(PositivePrimaryGradient)
                    .clickable { onAddToCalendar() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Add to Phone Calendar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
            currentAction = notice.action_needed,
            onDismiss = { showEditAction = false },
            onConfirm = { updatedAction -> onNoticeUpdated(notice.copy(action_needed = updatedAction)) }
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
            currentDate = notice.date,
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
