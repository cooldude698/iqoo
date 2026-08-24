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
import com.iqoo.noticesorter.model.NoticeType
import com.iqoo.noticesorter.ui.theme.*
import java.time.LocalDate

@Composable
fun EditTitleDialog(
    currentTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Edit Notice Title",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    "Enter a clear, descriptive title for this notice:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("e.g. Mid-Term Exam Schedule") },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandIndigo,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text.trim()); onDismiss() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
            ) {
                Text("Save Title", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun EditActionDialog(
    currentAction: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentAction) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Edit Action Required",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    "Specify what student action or task is required:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandIndigo,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text.trim()); onDismiss() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
            ) {
                Text("Save Action", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun EditTypeDialog(
    currentType: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedType by remember { mutableStateOf(currentType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Select Category",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NoticeType.entries.forEach { typeEnum ->
                    val isSelected = selectedType.lowercase() == typeEnum.name.lowercase()
                    Surface(
                        color = if (isSelected) typeEnum.containerColor else SurfaceCardSecondary,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = typeEnum.name.lowercase() }
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) typeEnum.contentColor else BorderSubtle,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedType = typeEnum.name.lowercase() },
                                colors = RadioButtonDefaults.colors(selectedColor = typeEnum.contentColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = typeEnum.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) typeEnum.contentColor else TextPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedType); onDismiss() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
            ) {
                Text("Select Category", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun EditDateDialog(
    currentDate: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var dateText by remember { mutableStateOf(currentDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Set Event Date",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    "Select a quick preset or type in YYYY-MM-DD:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Quick Date Preset Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val today = LocalDate.now()
                    QuickDateChip("Today") { dateText = today.toString() }
                    QuickDateChip("Tomorrow") { dateText = today.plusDays(1).toString() }
                    QuickDateChip("+7 Days") { dateText = today.plusDays(7).toString() }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    placeholder = { Text("YYYY-MM-DD (e.g. 2026-09-15)") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandIndigo,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(dateText.trim()); onDismiss() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
            ) {
                Text("Save Date", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun QuickDateChip(label: String, onClick: () -> Unit) {
    Surface(
        color = SurfaceCardSecondary,
        shape = CircleShape,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .border(1.dp, BorderSubtle, CircleShape)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = BrandIndigo,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun EditTimeDialog(
    currentTime: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var timeText by remember { mutableStateOf(currentTime ?: "09:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Set Event Time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    "Enter time in 24-hr format (HH:MM) or quick presets:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickDateChip("Morning (09:30)") { timeText = "09:30" }
                    QuickDateChip("Noon (12:00)") { timeText = "12:00" }
                    QuickDateChip("Evening (17:00)") { timeText = "17:00" }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    placeholder = { Text("HH:MM (e.g. 10:30)") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandIndigo,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(timeText.trim().ifBlank { null }); onDismiss() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
            ) {
                Text("Save Time", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
