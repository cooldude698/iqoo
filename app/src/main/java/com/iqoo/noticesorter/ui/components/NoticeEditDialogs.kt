package com.iqoo.noticesorter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iqoo.noticesorter.model.NoticeType

@Composable
fun EditTitleDialog(
    currentTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Title") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Notice Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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
        title = { Text("Edit Action Required") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Action Required") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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
        title = { Text("Select Notice Category") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                NoticeType.entries.forEach { typeEnum ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedType.lowercase() == typeEnum.name.lowercase()),
                                onClick = { selectedType = typeEnum.name.lowercase() }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedType.lowercase() == typeEnum.name.lowercase()),
                            onClick = { selectedType = typeEnum.name.lowercase() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = typeEnum.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedType); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateDialog(
    currentDate: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var dateText by remember { mutableStateOf(currentDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Event Date") },
        text = {
            Column {
                Text("Enter date in YYYY-MM-DD format:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    placeholder = { Text("2026-09-15") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(dateText); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
        title = { Text("Set Event Time") },
        text = {
            Column {
                Text("Enter time in 24-hr format (HH:MM):", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("Time (HH:MM)") },
                    placeholder = { Text("14:30") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timeText.ifBlank { null }); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
