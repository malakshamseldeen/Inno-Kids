package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LessonEntity
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.InnoMascotHeader
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.CyanElectric
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.InnoKidsViewModel

data class QuickActionItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val targetTab: String
)

@Composable
fun HomeScreen(
    viewModel: InnoKidsViewModel,
    onNavigateToTab: (String) -> Unit,
    onOpenLesson: (LessonEntity) -> Unit
) {
    val user by viewModel.userProfile.collectAsState()
    val lessons by viewModel.lessons.collectAsState()

    val currentMission = lessons.find { !it.isCompleted } ?: lessons.firstOrNull()

    val quickActions = listOf(
        QuickActionItem("Learn AI", "Interactive Lessons", Icons.Default.Psychology, listOf(NeonBlue, CyanElectric), "learn"),
        QuickActionItem("Play Games", "Fun AI Challenges", Icons.Default.Extension, listOf(NeonPurple, Color(0xFFC084FC)), "challenges"),
        QuickActionItem("Ask Inno", "AI Robot Companion", Icons.Default.SmartToy, listOf(Color(0xFF06B6D4), CyanGlow), "inno_chat"),
        QuickActionItem("AI Playground", "Create & Experiment", Icons.Default.Brush, listOf(Color(0xFFEC4899), Color(0xFFF43F5E)), "playground"),
        QuickActionItem("Rewards & Shop", "Badges & Avatars", Icons.Default.Face, listOf(AccentOrange, AccentGold), "profile"),
        QuickActionItem("Parent Area", "PIN Protected", Icons.Default.Lock, listOf(Color(0xFF475569), Color(0xFF1E293B)), "parent")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBackground)
    ) {
        item {
            InnoMascotHeader(user = user)
        }

        // Current Mission Banner
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Mission",
                        tint = CyanGlow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Current Learning Mission",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("current_mission_card"),
                    borderColor = CyanElectric.copy(alpha = 0.5f),
                    onClick = { currentMission?.let { onOpenLesson(it) } }
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_learning_banner_1784723540177),
                                contentDescription = "Learning Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color(0xEE0A0F2E))
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonPurple)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentMission?.category ?: "Beginner",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = currentMission?.title ?: "What is AI?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentMission?.description ?: "Discover how smart computers learn!",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "XP",
                                        tint = AccentGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "+${currentMission?.xpReward ?: 50} XP",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = AccentGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Button(
                                    onClick = { currentMission?.let { onOpenLesson(it) } },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("start_mission_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Start",
                                        tint = SpaceBackground,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Continue",
                                        color = SpaceBackground,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Access Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Explore InnoKids 🚀",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val rows = quickActions.chunked(2)
                    for (row in rows) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (action in row) {
                                GlassmorphicCard(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(105.dp)
                                        .testTag("quick_action_${action.targetTab}"),
                                    onClick = { onNavigateToTab(action.targetTab) }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Brush.linearGradient(action.gradientColors)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = action.icon,
                                                contentDescription = action.title,
                                                tint = TextPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = action.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            )
                                            Text(
                                                text = action.subtitle,
                                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Daily Robot Prompt & Fun Fact
        item {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                borderColor = NeonPurple.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_inno_mascot_1784723528045),
                        contentDescription = "Inno Fact",
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "🤖 Inno's Daily AI Tip",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = CyanGlow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"AI learns from practice just like you! The more good questions you ask, the smarter your brain gets!\"",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
