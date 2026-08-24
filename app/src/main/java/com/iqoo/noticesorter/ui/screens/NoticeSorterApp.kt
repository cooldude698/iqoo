package com.iqoo.noticesorter.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.noticesorter.data.NoticeProcessor
import com.iqoo.noticesorter.model.NoticeData
import com.iqoo.noticesorter.ui.components.CalendarLauncher
import com.iqoo.noticesorter.ui.components.NoticeCard
import com.iqoo.noticesorter.ui.theme.*

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
    var pickedImageUri by remember { mutableStateOf<String?>(null) }

    // Launcher for Prit's image upload picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            pickedImageUri = it.toString()
        }
    }

    // Launch notice processing when shared URI, picked URI, or mock changes
    LaunchedEffect(sharedImageUri, pickedImageUri, mockSelection) {
        uiState = AppUiState.LOADING
        val uriToProcess = sharedImageUri ?: pickedImageUri ?: mockSelection
        currentNotice = processor.processNotice(uriToProcess, context)
        uiState = AppUiState.RESULT_CARD
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PositivePrimaryGradient)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = PaletteCream,
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "N",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = PaletteSlateBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Notice Sorter",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Smart Education Track • OriginOS Phone-First",
                                style = MaterialTheme.typography.labelSmall,
                                color = PaletteSoftSteel
                            )
                        }
                    }

                    // Office Kit & On-Device Badge
                    Surface(
                        color = Color(0x33FFFFFF),
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phonelink,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Office Kit",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PaletteCream)
        ) {

            // Prit's "Digitize Your Notice" Upload Card & Sample Chips
            if (sharedImageUri == null) {
                PritDigitizeUploadCard(
                    onUploadClick = { galleryLauncher.launch("image/*") }
                )

                DemoNoticeSelectorBar(
                    selectedKey = mockSelection,
                    onSelect = { key ->
                        pickedImageUri = null
                        mockSelection = key
                    }
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
                                    .padding(vertical = 4.dp),
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

/**
 * Prit's "Digitize Your Notice" Card — Upload photo or PDF from gallery/files
 */
@Composable
fun PritDigitizeUploadCard(
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onUploadClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EEF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Upload Notice",
                        tint = PaletteSlateBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Digitize Your Notice (Prit's OCR)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PaletteDarkText
                    )
                    Text(
                        text = "Tap to upload notice photo or PDF from gallery",
                        style = MaterialTheme.typography.bodySmall,
                        color = PaletteSubtext
                    )
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
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "OR PREVIEW DEMO NOTICES",
                style = MaterialTheme.typography.labelSmall,
                color = PaletteSlateBlue,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DemoChip("Exam Notice", selectedKey == "exam") { onSelect("exam") }
                DemoChip("Fee Circular", selectedKey == "fee") { onSelect("fee") }
                DemoChip("Event", selectedKey == "event") { onSelect("event") }
                DemoChip("Needs Date", selectedKey == "low") { onSelect("low") }
            }
        }
    }
}

@Composable
fun DemoChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) PaletteSlateBlue else Color(0xFFEFF3F6))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.White else PaletteDarkText,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
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
                modifier = Modifier.size(56.dp),
                color = PaletteSlateBlue,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Reading your notice...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PaletteDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Extracting dates, deadlines & action items via on-device ML Kit & LLM",
                style = MaterialTheme.typography.bodyMedium,
                color = PaletteSubtext,
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
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF4EC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PaletteMossGreen,
                        modifier = Modifier.size(46.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Added to Calendar!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PaletteDarkText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${notice.title}\" has been scheduled for ${notice.date} ${notice.time ?: ""} with a 24-hr reminder.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PaletteSubtext,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(28.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(PositivePrimaryGradient)
                        .clickable { onReset() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Back to Result Card",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
