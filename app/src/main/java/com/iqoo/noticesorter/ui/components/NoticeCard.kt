package com.iqoo.noticesorter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.iqoo.noticesorter.ui.theme.WarningBannerColor
import com.iqoo.noticesorter.ui.theme.WarningTextColor

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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            // Top Category Tag & Confidence Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = noticeType.containerColor,
                    contentColor = noticeType.contentColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { showEditType = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
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
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Category",
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                if (notice.isLowConfidence) {
                    Surface(
                        color = WarningBannerColor,
                        contentColor = WarningTextColor,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Needs Review",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notice Title (Large & Editable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showEditTitle = true }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.title.ifBlank { "Untitled Notice" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Title",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent Date & Time Card Block
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), shape = RoundedCornerShape(12.dp))
                    .padding(14.dp),
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
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "EVENT DATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = if (notice.date.isNotBlank()) notice.date else "Tap to set date",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (notice.date.isNotBlank()) Color.Unspecified else WarningTextColor
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )

                // Time
                Row(
                    modifier = Modifier
                        .clickable { showEditTime = true }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "TIME",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = notice.time ?: "All Day",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Low Confidence Warning Prompt Banner
            if (notice.isLowConfidence) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarningBannerColor, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = WarningTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Couldn't detect exact date cleanly. Tap date block above to select.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Needed (Editable)
            Text(
                text = "ACTION REQUIRED",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showEditAction = true }
                    .background(Color(0xFFFAFAFA))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.action_needed.ifBlank { "No action specified. Tap to add details." },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Action",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Add to Calendar CTA
            Button(
                onClick = onAddToCalendar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E1E24), // iQOO Dark Header Accent
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Add to Calendar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
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
