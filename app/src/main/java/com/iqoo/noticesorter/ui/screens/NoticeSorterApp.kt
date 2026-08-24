package com.iqoo.noticesorter.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.noticesorter.data.NoticeProcessor
import com.iqoo.noticesorter.model.NoticeData
import com.iqoo.noticesorter.ui.components.CalendarLauncher
import com.iqoo.noticesorter.ui.components.NoticeCard
import com.iqoo.noticesorter.ui.theme.IQOODarkHeader
import com.iqoo.noticesorter.ui.theme.IQOOYellow

enum class AppUiState {
    LOADING,
    RESULT_CARD,
    CONFIRMED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeSorterApp(
    sharedImageUri: String?,
    processor: NoticeProcessor
) {
    val context = LocalContext.current
    var uiState by remember { mutableStateOf(AppUiState.LOADING) }
    var currentNotice by remember { mutableStateOf<NoticeData?>(null) }
    var mockSelection by remember { mutableStateOf("exam") }

    // Launch notice processing when URI or mock changes
    LaunchedEffect(sharedImageUri, mockSelection) {
        uiState = AppUiState.LOADING
        val uriToProcess = sharedImageUri ?: mockSelection
        currentNotice = processor.processNotice(uriToProcess)
        uiState = AppUiState.RESULT_CARD
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = IQOOYellow,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "N",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Notice Sorter",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "OriginOS AI Screen Extension",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IQOODarkHeader)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Demo notice selector bar (for manual preview when app launched directly without share intent)
            if (sharedImageUri == null) {
                DemoNoticeSelectorBar(
                    selectedKey = mockSelection,
                    onSelect = { key -> mockSelection = key }
                )
            }

            AnimatedContent(
                targetState = uiState,
                label = "ScreenTransition"
            ) { targetState ->
                when (targetState) {
                    AppUiState.LOADING -> LoadingScreen()
                    AppUiState.RESULT_CARD -> {
                        currentNotice?.let { notice ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                NoticeCard(
                                    notice = notice,
                                    onNoticeUpdated = { updated -> currentNotice = updated },
                                    onAddToCalendar = {
                                        val launched = CalendarLauncher.launchCalendarEvent(context, notice)
                                        if (launched) {
                                            uiState = AppUiState.CONFIRMED
                                        }
                                    }
                                )
                            }
                        }
                    }
                    AppUiState.CONFIRMED -> {
                        currentNotice?.let { notice ->
                            ConfirmationScreen(
                                notice = notice,
                                onReset = { uiState = AppUiState.RESULT_CARD }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DemoNoticeSelectorBar(
    selectedKey: String,
    onSelect: (String) -> Unit
) {
    Surface(
        color = Color(0xFF2D2D35),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "DEMO SAMPLE NOTICES",
                style = MaterialTheme.typography.labelSmall,
                color = IQOOYellow,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedKey == "exam",
                    onClick = { onSelect("exam") },
                    label = { Text("Exam Notice") }
                )
                FilterChip(
                    selected = selectedKey == "fee",
                    onClick = { onSelect("fee") },
                    label = { Text("Fee Circular") }
                )
                FilterChip(
                    selected = selectedKey == "event",
                    onClick = { onSelect("event") },
                    label = { Text("Event") }
                )
                FilterChip(
                    selected = selectedKey == "low",
                    onClick = { onSelect("low") },
                    label = { Text("Low Confidence") }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(54.dp),
                color = IQOODarkHeader,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Reading your notice...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Extracting dates, deadlines & action items via ML Kit & LLM",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ConfirmationScreen(
    notice: NoticeData,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Added to Calendar!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "\"${notice.title}\" has been scheduled for ${notice.date} ${notice.time ?: ""} with a 24-hr reminder.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Back to Result Card")
            }
        }
    }
}
