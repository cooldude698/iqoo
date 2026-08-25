package com.collegeos.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.collegeos.core.common.Constants
import com.collegeos.feature.academics.AcademicsViewModel
import com.collegeos.feature.academics.ui.AcademicsScreen
import com.collegeos.feature.noticesorter.data.RealNoticeProcessor
import com.collegeos.feature.noticesorter.ui.screens.NoticeSorterApp
import com.collegeos.feature.social.SocialViewModel
import com.collegeos.feature.social.ui.SocialScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    initialSharedUri: String? = null
) {
    var selectedTab by remember { mutableIntStateOf(if (initialSharedUri != null) 1 else 0) }
    val academicsViewModel = remember { AcademicsViewModel() }
    val socialViewModel = remember { SocialViewModel() }
    val noticeProcessor = remember { RealNoticeProcessor() }

    val academicsState = academicsViewModel.uiState.value
    val socialState = socialViewModel.uiState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF4F46E5), Color(0xFF3B82F6))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.School,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = Constants.APP_NAME,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.Verified,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF4F46E5),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = Constants.APP_TAGLINE,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        color = Color(0xFFECFDF5),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ERP SYNCED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Home", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Notice Sorter", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = "Notice Sorter", tint = if (selectedTab == 1) Color(0xFF4F46E5) else MaterialTheme.colorScheme.onSurfaceVariant) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("Academics", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Filled.School, contentDescription = "Academics") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    label = { Text("Clubs", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Filled.Groups, contentDescription = "Clubs") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    label = { Text("Profile", fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> UnifiedDashboardScreen(
                    onNavigateToNoticeSorter = { selectedTab = 1 },
                    onNavigateToAcademics = { selectedTab = 2 },
                    onNavigateToSocial = { selectedTab = 3 }
                )
                1 -> NoticeSorterApp(
                    sharedImageUri = initialSharedUri,
                    processor = noticeProcessor
                )
                2 -> AcademicsScreen(state = academicsState)
                3 -> CommunitiesAndClubsScreen(socialState)
                4 -> ProfileAndSettingsScreen()
            }
        }
    }
}

@Composable
fun UnifiedDashboardScreen(
    onNavigateToNoticeSorter: () -> Unit,
    onNavigateToAcademics: () -> Unit,
    onNavigateToSocial: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Feature Banner: AI Notice Sorter & OriginOS AI Vision
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 6.dp,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFF22C55E).copy(alpha = 0.2f),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, Color(0xFF4ADE80))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color(0xFF22C55E), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "OriginOS AI Vision Active",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF86EFAC),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "v1.0.0 Live",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA5B4FC)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Welcome back, Alex Chen! 👋",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Share any college notice photo or PDF from WhatsApp into Notice Sorter to extract deadlines and sync them to your phone calendar in 1 tap.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFE0E7FF),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToNoticeSorter,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Open AI Notice Sorter & Calendar Sync",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 2. Today's Class Schedule (6 Classes Timeline)
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Class, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Today's Schedule (6 Classes)",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Wednesday",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Class 1 (NOW)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                            Text("NOW • 09:00", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CS301 • Data Structures & Algorithms", fontWeight = FontWeight.Bold)
                            Text("Lab 2 • Dr. Rajesh Kumar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Class 2
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                            Text("10:15 AM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold, color = Color(0xFF475569), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CS302 • Database Management Systems", fontWeight = FontWeight.Bold)
                            Text("LH 104 • Prof. Ananya Sharma", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Class 3
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                            Text("11:30 AM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold, color = Color(0xFF475569), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MA301 • Linear Algebra & Probability", fontWeight = FontWeight.Bold)
                            Text("LH 201 • Dr. V. K. Raman", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Class 4
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                            Text("01:30 PM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold, color = Color(0xFF475569), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("EC204 • Digital Systems & Microprocessors", fontWeight = FontWeight.Bold)
                            Text("Micro Lab • Prof. S. N. Roy", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Class 5
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                            Text("02:45 PM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold, color = Color(0xFF475569), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CS305 • Operating Systems & Kernel", fontWeight = FontWeight.Bold)
                            Text("LH 102 • Dr. Meera Joshi", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Class 6
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                            Text("04:00 PM", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.SemiBold, color = Color(0xFF475569), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AI401 • Machine Learning & Neural Nets", fontWeight = FontWeight.Bold)
                            Text("AI Studio • Dr. Vikram Seth", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = onNavigateToAcademics,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("View Full Weekly Timetable & Room Map")
                    }
                }
            }
        }

        // 3. Academic & Attendance Overview Grid (4 Cards)
        item {
            Text(
                text = "Academic & Attendance Overview",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier.weight(1f).clickable { onNavigateToAcademics() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📊 Attendance", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("90.8%", color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(progress = { 0.908f }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape), color = Color(0xFF10B981))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Safe (+5 classes margin)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f).clickable { onNavigateToAcademics() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📝 Assignments", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("4 Due", color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(progress = { 0.5f }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape), color = Color(0xFFD97706))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("B-Tree Due Tomorrow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        modifier = Modifier.weight(1f).clickable { onNavigateToSocial() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💬 Campus Social", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("12 New", color = Color(0xFF8B5CF6), fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("TechFest & Coding Club", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f).clickable { onNavigateToNoticeSorter() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🚀 Hackathons", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text("6 Tracked", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("iQOO Hackathon Aug 30", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }

        // 4. Upcoming Hackathons & Tech Events (5 Hackathons Cards)
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upcoming Midterm Exams & Hackathons",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Hackathon 1
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFC7D2FE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏆 iQOO OriginOS City Battle Hackathon 2026", fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(color = Color(0xFF4F46E5), shape = CircleShape) {
                                Text("Aug 30", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Date: Aug 30, 2026 @ 10:00 AM | Track: Smart Education | Prize Pool: ₹1,00,000", style = MaterialTheme.typography.bodySmall, color = Color(0xFF374151))
                    }
                }

                // Hackathon 2
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🚀 Smart India Hackathon 2026 — State Finals", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(color = Color(0xFF10B981), shape = CircleShape) {
                                Text("Sep 15", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Date: Sep 15, 2026 @ 09:00 AM | Problem Statement Submission Open", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }

                // Hackathon 3
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡ Google Cloud Generative AI CodeSprint", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(color = Color(0xFF2563EB), shape = CircleShape) {
                                Text("Sep 22", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Date: Sep 22, 2026 @ 11:00 AM | Vertex AI & Gemini Nano Track", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun CommunitiesAndClubsScreen(socialState: com.collegeos.feature.social.SocialState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Verified Clubs & Campus Communities",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Official institution-recognized student clubs and active interest groups.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        item {
            Text("Verified Institution Clubs (${socialState.clubs.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        items(socialState.clubs) { club ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(club.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(color = Color(0xFFECFDF5), shape = CircleShape, border = BorderStroke(1.dp, Color(0xFFA7F3D0))) {
                            Text("✓ Verified Club", color = Color(0xFF047857), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Category: ${club.category} • ${club.membersCount} Active Members", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(club.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Faculty Advisor: ${club.facultyAdvisor}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF4F46E5), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Club Portal & Join Events")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Campus Interest Communities (${socialState.communities.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        items(socialState.communities) { comm ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(comm.name, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        if (comm.isJoined) {
                            Surface(color = Color(0xFFEEF2FF), shape = CircleShape) {
                                Text("Joined ✓", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(comm.description, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${comm.membersCount} Members • ${comm.category}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun ProfileAndSettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "My Identity & Account",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4F46E5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AC", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Alex Chen", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("USN: 2024CS108", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Filled.Verified, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Department: Department of Computer Science & Engineering", style = MaterialTheme.typography.bodyMedium)
                    Text("Program: B.Tech Computer Science & Engineering", style = MaterialTheme.typography.bodyMedium)
                    Text("Current Academic Year: 2nd Year (Semester 3)", style = MaterialTheme.typography.bodyMedium)
                    Text("Cumulative GPA (CGPA): 8.92 / 10.0", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                    Text("Earned Credits: 94 / 160 Required Credits", style = MaterialTheme.typography.bodyMedium)
                    Text("Institutional Email: alex.chen@campus.edu", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Sync, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Campus ERP Integration Status", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Provider: Enterprise Campus ERP Connector v2.4", color = MaterialTheme.colorScheme.onSurface)
                    Text("Status: CONNECTED & SYNCED", color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                    Text("Last Incremental Sync: Today @ 23:25")
                    Text("Circuit Breaker Status: CLOSED (Healthy 100%)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Security, contentDescription = null, tint = Color(0xFF4F46E5))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Security & Privacy Settings", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Social Visibility: COLLEGE (Campus Scoped)")
                    Text("Token Refresh Engine: SHA-256 Rotation Active")
                    Text("Multi-Factor Authentication: Enabled ✓")
                }
            }
        }
    }
}
