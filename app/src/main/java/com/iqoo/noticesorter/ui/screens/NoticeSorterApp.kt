package com.iqoo.noticesorter.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
    val haptic = LocalHapticFeedback.current

    var uiState by remember { mutableStateOf(AppUiState.LOADING) }
    var currentNotice by remember { mutableStateOf<NoticeData?>(null) }
    var mockSelection by remember { mutableStateOf("exam") }
    var pickedImageUri by remember { mutableStateOf<String?>(null) }

    // Launcher for file picker (Images & PDFs)
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
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
        containerColor = CanvasBackground,
        topBar = {
            Surface(
                color = SurfaceCard,
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC))
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // High-tech Gradient App Icon
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ActionButtonGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Notice Sorter AI",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(FeeEmerald)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "OriginOS 5.0 • Vision AI Engine",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Real-time Status Badge
                        Surface(
                            color = Color(0xFFEEF2FF),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color(0xFFC7D2FE))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = BrandIndigo,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Gemini Vision",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandIndigo,
                                    fontSize = 10.sp
                                )
                            }
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
                .background(CanvasBackground)
        ) {

            // Smart Ingestion Hub & Presets (Visible when no external intent)
            if (sharedImageUri == null) {
                SmartNoticeIngestionHub(
                    onUploadClick = { fileLauncher.launch("*/*") }
                )

                SampleNoticeSelector(
                    selectedKey = mockSelection,
                    onSelect = { key ->
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
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
                    AppUiState.LOADING -> ModernLoadingScreen()
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
 * Smart Scan & Document Ingestion Hub
 */
@Composable
fun SmartNoticeIngestionHub(
    onUploadClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x140F172A)
            ),
        shape = RoundedCornerShape(22.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = BrandIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Digitize Campus Notice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Upload photo or PDF circular",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Ingest CTA Button
                Surface(
                    color = BrandIndigo,
                    shape = CircleShape,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onUploadClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Select File",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // File format pill badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FormatBadge("🖼️ Photos / Screenshots")
                FormatBadge("📄 PDF Circulars")
                FormatBadge("⚡ Auto OCR + Dates")
            }
        }
    }
}

@Composable
fun FormatBadge(label: String) {
    Surface(
        color = SurfaceCardSecondary,
        shape = CircleShape,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * Sample Notices Segmented Selector Bar
 */
@Composable
fun SampleNoticeSelector(
    selectedKey: String,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRY WITH SAMPLE NOTICES",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SampleChip("📝 Exam", isSelected = selectedKey == "exam") { onSelect("exam") }
            SampleChip("💰 Fee Dues", isSelected = selectedKey == "fee") { onSelect("fee") }
            SampleChip("🚀 Event", isSelected = selectedKey == "event") { onSelect("event") }
            SampleChip("⚠️ Blurry", isSelected = selectedKey == "low") { onSelect("low") }
        }
    }
}

@Composable
fun SampleChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) BrandIndigo else SurfaceCard,
        contentColor = if (isSelected) Color.White else TextPrimary,
        shape = CircleShape,
        border = BorderStroke(1.dp, if (isSelected) BrandIndigo else BorderSubtle),
        shadowElevation = if (isSelected) 3.dp else 0.dp,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}

/**
 * Modern High-Tech Loading Screen with Animated Scanner
 */
@Composable
fun ModernLoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "LoadingPulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(54.dp),
                    color = BrandIndigo,
                    strokeWidth = 3.5.dp
                )
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = BrandIndigo,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AI Notice Intelligence Active",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Running on-device OCR & extracting deadlines via Gemini Vision...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step Progress Pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProcessingStepPill("1. OCR Text", isComplete = true)
                ProcessingStepPill("2. LLM Extraction", isComplete = true)
                ProcessingStepPill("3. Calendar Payload", isComplete = false, alpha = alpha)
            }
        }
    }
}

@Composable
fun ProcessingStepPill(label: String, isComplete: Boolean, alpha: Float = 1f) {
    Surface(
        color = if (isComplete) Color(0xFFECFDF5) else Color(0xFFEEF2FF),
        shape = CircleShape,
        border = BorderStroke(1.dp, if (isComplete) Color(0xFFA7F3D0) else Color(0xFFC7D2FE)),
        modifier = Modifier.clip(CircleShape)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isComplete) FeeEmerald else BrandIndigo.copy(alpha = alpha),
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * High-End Confirmation & Event Scheduled Screen
 */
@Composable
fun ConfirmationScreen(
    notice: NoticeData,
    onReset: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(Unit) {
        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = SurfaceCard,
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge with Pulse
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFECFDF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FeeEmerald,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Added to Calendar!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"${notice.title}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandIndigo,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Scheduled for ${notice.date ?: "Upcoming"} at ${notice.time ?: "09:00 AM"} with a 24-hr reminder alert.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Primary CTA to return or scan next
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ActionButtonGradient)
                        .clickable { onReset() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Back to Notice Card",
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

