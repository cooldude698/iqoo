package com.collegeos.feature.social.ui

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
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.collegeos.feature.social.ClubUiItem
import com.collegeos.feature.social.CommunityUiItem
import com.collegeos.feature.social.PostUiItem
import com.collegeos.feature.social.SocialState

@Composable
fun SocialScreen(
    state: SocialState
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Campus Feed", "Communities", "Verified Clubs", "Discover")

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
                    .background(Color(0xFF8B5CF6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Forum, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Campus Social OS",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Verified Clubs, Student Groups & Discussion Feeds",
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
                            color = if (selectedTabIndex == index) Color(0xFF8B5CF6) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTabIndex) {
            0 -> FeedList(state.feedPosts)
            1 -> CommunityList(state.communities)
            2 -> ClubList(state.clubs)
            3 -> DiscoverList()
        }
    }
}

@Composable
fun FeedList(items: List<PostUiItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF8B5CF6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                post.authorName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(post.authorName, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Filled.Verified, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(14.dp))
                            }
                            Text("${post.authorHandle} • ${post.authorRoleBadge}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                        Text(post.timeAgoText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(post.content, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {}) {
                            Icon(
                                if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (post.isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text("${post.likesCount}", style = MaterialTheme.typography.labelMedium)

                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Comment, contentDescription = "Comment", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${post.commentsCount}", style = MaterialTheme.typography.labelMedium)

                        Spacer(modifier = Modifier.weight(1f))

                        post.tag?.let {
                            Surface(color = Color(0xFFF3E8FF), shape = CircleShape) {
                                Text(it, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontWeight = FontWeight.Bold, color = Color(0xFF8B5CF6), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityList(items: List<CommunityUiItem>) {
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
                        Text(item.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        if (item.isJoined) {
                            Surface(color = Color(0xFFEEF2FF), shape = CircleShape) {
                                Text("Joined ✓", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5), fontSize = 10.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("${item.membersCount} Members • ${item.category}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun ClubList(items: List<ClubUiItem>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items) { club ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                    Text("Faculty Advisor: ${club.facultyAdvisor}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF8B5CF6), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Club Portal & Join Events")
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverList() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                border = BorderStroke(1.dp, Color(0xFFC7D2FE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔥 Trending Campus Discussions", fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("#iQOOHackathon #TechFest2026 #DBMSNormalisation #DataStructuresLab", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
