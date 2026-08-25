package com.collegeos.feature.academics.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.collegeos.feature.academics.AcademicsState
import com.collegeos.feature.academics.AssignmentUiItem
import com.collegeos.feature.academics.AttendanceSummaryItem
import com.collegeos.feature.academics.ExamScheduleItem
import com.collegeos.feature.academics.NoteUiItem
import com.collegeos.feature.academics.ResultItem
import com.collegeos.feature.academics.StudyTaskUiItem
import com.collegeos.feature.academics.TimetableItem

@Composable
fun AcademicsScreen(
    state: AcademicsState
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Dashboard", "Attendance", "Timetable", "Assignments", "Planner", "Exams", "Results", "Notes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF4F46E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.School, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Academics Operating System",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Real-time Attendance, Timetables, Assignments & Results • ${state.lastUpdatedText}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) Color(0xFF4F46E5) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTabIndex) {
            0 -> AcademicDashboardView(state)
            1 -> AttendanceList(state.attendance)
            2 -> TimetableList(state.timetable)
            3 -> AssignmentList(state.assignments)
            4 -> StudyTaskList(state.studyTasks)
            5 -> ExamList(state.exams)
            6 -> ResultList(state.results)
            7 -> NoteList(state.notes)
        }
    }
}

@Composable
fun AcademicDashboardView(state: AcademicsState) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Semester Overview: Semester 3 (2026-2027)", fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Credits Earned: 94 / 160 Required • CGPA: 8.92 / 10.0", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.908f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Overall Attendance: 90.8% (Safe +5 classes margin)", style = MaterialTheme.typography.bodySmall, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Text("Upcoming Assignments & Deadlines", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        items(state.assignments) { asg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                            Text(asg.subjectCode, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(asg.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(asg.description, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(asg.dueAtText, style = MaterialTheme.typography.labelSmall, color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Max Marks: ${asg.maxMarks.toInt()}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceList(items: List<AttendanceSummaryItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${item.subjectCode} • ${item.subjectName}", fontWeight = FontWeight.Bold)
                            Text("Attended ${item.attendedClasses} / ${item.totalClasses} Classes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                        Text("${item.percentage.toInt()}%", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (item.percentage >= 85f) Color(0xFF10B981) else Color(0xFFD97706))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { item.percentage / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = if (item.percentage >= 85f) Color(0xFF10B981) else Color(0xFFD97706)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(item.statusText, style = MaterialTheme.typography.labelSmall, color = if (item.percentage >= 85f) Color(0xFF047857) else Color(0xFFB45309), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TimetableList(items: List<TimetableItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFEEF2FF), shape = RoundedCornerShape(8.dp)) {
                            Text("${item.startTime} - ${item.endTime}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("${item.subjectCode} • ${item.subjectName}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Faculty: ${item.facultyName} | Room: ${item.room ?: "TBA"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun AssignmentList(items: List<AssignmentUiItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Surface(color = if (item.isSubmitted) Color(0xFFECFDF5) else Color(0xFFFEF3C7), shape = CircleShape) {
                            Text(if (item.isSubmitted) "Submitted ✓" else "Pending", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontWeight = FontWeight.Bold, color = if (item.isSubmitted) Color(0xFF047857) else Color(0xFFB45309), fontSize = 10.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${item.subjectCode} • ${item.dueAtText}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun StudyTaskList(items: List<StudyTaskUiItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Bold)
                        Text("${item.subjectCode} • Est. ${item.estimatedMins} mins", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Surface(color = Color(0xFFEEF2FF), shape = CircleShape) {
                        Text(item.priority, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ExamList(items: List<ExamScheduleItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${item.subjectCode} • ${item.subjectName}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Surface(color = Color(0xFFFEF3C7), shape = CircleShape) {
                            Text(item.examName, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontWeight = FontWeight.Bold, color = Color(0xFFB45309), fontSize = 10.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Date: ${item.date} | Time: ${item.time} | Room: ${item.room ?: "TBA"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun ResultList(items: List<ResultItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${item.subjectCode} • ${item.subjectName}", fontWeight = FontWeight.Bold)
                        Text(item.examName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${item.marks} / ${item.maxMarks}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        item.grade?.let {
                            Text("Grade: $it", fontWeight = FontWeight.Bold, color = Color(0xFF10B981), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteList(items: List<NoteUiItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(item.updatedAtText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.content, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
