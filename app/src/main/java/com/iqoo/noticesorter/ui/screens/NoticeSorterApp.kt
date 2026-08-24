package com.iqoo.noticesorter.ui.screens

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AppUiState {
    SPLASH,
    IDLE,
    LOADING,
    RESULT_CARD,
    CONFIRMED,
    ERROR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeSorterApp(
    sharedImageUri: String?,
    processor: NoticeProcessor
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    // Start with SPLASH on normal launch, or LOADING if opened directly via share intent
    var uiState by rememberSaveable { 
        mutableStateOf(if (sharedImageUri != null) AppUiState.LOADING else AppUiState.SPLASH) 
    }
    var currentNotice by remember { mutableStateOf<NoticeData?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var lastProcessedTarget by remember { mutableStateOf<String?>(null) }

    // Helper function to process notices cleanly
    fun processTarget(uriOrMock: String) {
        lastProcessedTarget = uriOrMock
        uiState = AppUiState.LOADING
        errorMessage = ""
        coroutineScope.launch {
            try {
                currentNotice = processor.processNotice(uriOrMock, context)
                uiState = AppUiState.RESULT_CARD
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to process notice."
                uiState = AppUiState.ERROR
            }
        }
    }

    // Process shared intent automatically if it exists on launch
    LaunchedEffect(sharedImageUri) {
        sharedImageUri?.let { processTarget(it) }
    }

    // Launcher for file picker (Images)
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            processTarget(it.toString())
        }
    }

    // Show branded splash screen on fresh launch
    if (uiState == AppUiState.SPLASH) {
        SplashScreen(
            onSplashFinished = {
                uiState = AppUiState.IDLE
            }
        )
        return
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
                                        text = "OriginOS · Vision AI",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        maxLines = 1
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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {

            // 1. ONLY SHOW INGESTION HUB, SELECTOR & HOW IT WORKS WHEN IN IDLE STATE
            if (uiState == AppUiState.IDLE) {
                SmartNoticeIngestionHub(
                    onUploadClick = { fileLauncher.launch("image/*") }
                )

                SampleNoticeSelector(
                    selectedKey = "",
                    onSelect = { key ->
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        processTarget(key)
                    }
                )

                HowItWorksSection()
            }

            // 2. ANIMATED TRANSITION FOR ACTIVE PROCESSING STATES
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(350)) + slideInVertically(
                        initialOffsetY = { it / 6 },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "ScreenTransition"
            ) { targetState ->
                when (targetState) {
                    AppUiState.SPLASH, AppUiState.IDLE -> { /* Handled */ }
                    AppUiState.LOADING -> ModernLoadingScreen()
                    AppUiState.RESULT_CARD -> {
                        currentNotice?.let { notice ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
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

                                Spacer(modifier = Modifier.height(16.dp))

                                // Secondary Action to discard & return to Hub
                                TextButton(
                                    onClick = { 
                                        if (sharedImageUri != null) {
                                            (context as? Activity)?.finish()
                                        } else {
                                            uiState = AppUiState.IDLE 
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (sharedImageUri != null) "Close & Return" else "Cancel & Scan Another",
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                    AppUiState.CONFIRMED -> {
                        currentNotice?.let { notice ->
                            ConfirmationScreen(
                                notice = notice,
                                onReset = { uiState = AppUiState.RESULT_CARD },
                                onShareAnother = {
                                    currentNotice = null
                                    if (sharedImageUri != null) {
                                        (context as? Activity)?.finish()
                                    } else {
                                        uiState = AppUiState.IDLE
                                    }
                                }
                            )
                        }
                    }
                    AppUiState.ERROR -> {
                        ErrorScreen(
                            errorMessage = errorMessage,
                            onRetry = {
                                lastProcessedTarget?.let { processTarget(it) } ?: run { uiState = AppUiState.IDLE }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Smart Scan & Document Ingestion Hub (Full-Width Primary Layout)
 */
@Composable
fun SmartNoticeIngestionHub(
    onUploadClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x1A0F172A)
            ),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Top row: icon + title (full width)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DocumentScanner,
                        contentDescription = null,
                        tint = BrandIndigo,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Digitize Campus Notice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Upload a photo or screenshot of any notice",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Full-width CTA button
            Button(
                onClick = onUploadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Select Photo",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Format badges — scrollable, never clip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormatBadge("🖼️  Photos / Screenshots")
                FormatBadge("📄  PDF via Share")
                FormatBadge("⚡  Auto OCR + Dates")
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
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Sample Notices 2x2 Grid Selector
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
        Text(
            text = "TRY WITH SAMPLE NOTICES",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
        )

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SampleChip(
                label = "📝  Exam",
                isSelected = selectedKey == "exam",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("exam") }
            )
            SampleChip(
                label = "💰  Fee Dues",
                isSelected = selectedKey == "fee",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("fee") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SampleChip(
                label = "🚀  Event",
                isSelected = selectedKey == "event",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("event") }
            )
            SampleChip(
                label = "⚠️  Blurry",
                isSelected = selectedKey == "low",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("low") }
            )
        }
    }
}

@Composable
fun SampleChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) BrandIndigo else SurfaceCard,
        contentColor = if (isSelected) Color.White else TextPrimary,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.5.dp,
            if (isSelected) BrandIndigo else BorderSubtle
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

/**
 * High-End "How It Works" Section
 */
@Composable
fun HowItWorksSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = "HOW IT WORKS",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                HowItWorksStep(
                    number = "1",
                    title = "Share or Upload",
                    subtitle = "Forward any notice from WhatsApp",
                    color = BrandIndigo
                )
                HorizontalDivider(
                    color = BorderSubtle,
                    modifier = Modifier.padding(start = 52.dp)
                )
                HowItWorksStep(
                    number = "2",
                    title = "AI Extracts Details",
                    subtitle = "Date, time, type & action needed",
                    color = Color(0xFF8B5CF6)
                )
                HorizontalDivider(
                    color = BorderSubtle,
                    modifier = Modifier.padding(start = 52.dp)
                )
                HowItWorksStep(
                    number = "3",
                    title = "Add to Calendar",
                    subtitle = "One tap with automatic reminder",
                    color = FeeEmerald,
                    showDivider = false
                )
            }
        }
    }
}

@Composable
fun HowItWorksStep(
    number: String,
    title: String,
    subtitle: String,
    color: Color,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

/**
 * Modern High-Tech Loading Screen with Real Sequential Step Transitions
 */
@Composable
fun ModernLoadingScreen() {
    var currentStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        delay(600)
        currentStep = 1
        delay(1200)
        currentStep = 2
    }

    val infiniteTransition = rememberInfiniteTransition(label = "LoadingPulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
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
                text = when (currentStep) {
                    0 -> "1/3: Reading image text with Google ML Kit OCR..."
                    1 -> "2/3: Analyzing deadlines & action items with Gemini Vision..."
                    else -> "3/3: Validating structure & preparing calendar event..."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Step Progress Pills with sequential lighting
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProcessingStepPill(
                    label = "1. OCR Text",
                    isComplete = currentStep >= 1,
                    alpha = if (currentStep == 0) alpha else 1f
                )
                ProcessingStepPill(
                    label = "2. LLM Extraction",
                    isComplete = currentStep >= 2,
                    alpha = if (currentStep == 1) alpha else 1f
                )
                ProcessingStepPill(
                    label = "3. Calendar Payload",
                    isComplete = false,
                    alpha = if (currentStep >= 2) alpha else 0.4f
                )
            }
        }
    }
}

@Composable
fun ProcessingStepPill(label: String, isComplete: Boolean, alpha: Float = 1f) {
    Surface(
        color = if (isComplete) Color(0xFFECFDF5) else Color(0xFFEEF2FF).copy(alpha = alpha),
        shape = CircleShape,
        border = BorderStroke(1.dp, if (isComplete) Color(0xFFA7F3D0) else Color(0xFFC7D2FE)),
        modifier = Modifier.clip(CircleShape)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isComplete) FeeEmerald else BrandIndigo,
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
    onReset: () -> Unit,
    onShareAnother: () -> Unit
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

                Spacer(modifier = Modifier.height(24.dp))

                // Primary CTA to return
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
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

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary CTA to share another notice
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCardSecondary,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onShareAnother() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = BrandIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan / Share Another Notice",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandIndigo
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern High-Tech Error Screen with Retry
 */
@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceCard,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, Color(0xFFFECACA)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Couldn't Process Notice",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandIndigo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onRetry() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Try Again",
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



