package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LessonEntity
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CyanElectric
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.InnoKidsViewModel
import org.json.JSONArray

@Composable
fun LearnScreen(
    viewModel: InnoKidsViewModel,
    onLessonOpened: (LessonEntity) -> Unit
) {
    val lessons by viewModel.lessons.collectAsState()
    val selectedLesson by viewModel.selectedLesson.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Beginner", "Intermediate", "Advanced")

    val filteredLessons = lessons.filter { lesson ->
        val matchesCategory = (selectedCategory == "All") || (lesson.category.equals(selectedCategory, ignoreCase = true))
        val matchesSearch = searchQuery.isEmpty() ||
                lesson.title.contains(searchQuery, ignoreCase = true) ||
                lesson.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "AI Academy 🎓",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Interactive lessons designed for young innovators!",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search lessons (e.g., Prompts, Vision)...", color = TextSecondary) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = CyanGlow) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lesson_search_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanElectric,
                        unfocusedBorderColor = Color(0xFF20326B),
                        focusedContainerColor = SpaceCardBg,
                        unfocusedContainerColor = SpaceCardBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = (selectedCategory == cat)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) CyanElectric else SpaceCardBg)
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("category_tab_$cat")
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) SpaceBackground else TextPrimary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Lessons List
            items(filteredLessons) { lesson ->
                LessonCardItem(
                    lesson = lesson,
                    onClick = { viewModel.selectLesson(lesson) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Selected Lesson Dialog
        selectedLesson?.let { lesson ->
            LessonDetailDialog(
                lesson = lesson,
                onDismiss = { viewModel.selectLesson(null) },
                onComplete = {
                    viewModel.completeLesson(lesson.id)
                    viewModel.selectLesson(null)
                }
            )
        }
    }
}

@Composable
fun LessonCardItem(
    lesson: LessonEntity,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lesson_card_${lesson.id}"),
        borderColor = if (lesson.isCompleted) AccentGreen.copy(alpha = 0.5f) else CyanElectric.copy(alpha = 0.3f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (lesson.isCompleted) AccentGreen.copy(alpha = 0.2f) else NeonBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (lesson.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = AccentGreen,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = CyanElectric,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (lesson.category) {
                                    "Beginner" -> AccentGreen.copy(alpha = 0.2f)
                                    "Intermediate" -> NeonBlue.copy(alpha = 0.2f)
                                    else -> NeonPurple.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = lesson.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = when (lesson.category) {
                                    "Beginner" -> AccentGreen
                                    "Intermediate" -> CyanElectric
                                    else -> NeonPurple
                                },
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${lesson.estMinutes}m",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP",
                        tint = AccentGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "+${lesson.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = AccentGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "+${lesson.coinReward} Coins",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LessonDetailDialog(
    lesson: LessonEntity,
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    var quizStep by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isQuizAnsweredCorrectly by remember { mutableStateOf<Boolean?>(null) }

    // Parse Quiz JSON
    val quizList = remember(lesson.quizQuestionsJson) {
        val list = mutableListOf<Triple<String, List<String>, Int>>()
        try {
            val array = JSONArray(lesson.quizQuestionsJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val q = obj.getString("question")
                val optsArray = obj.getJSONArray("options")
                val opts = mutableListOf<String>()
                for (j in 0 until optsArray.length()) {
                    opts.add(optsArray.getString(j))
                }
                val correct = obj.getInt("correctIndex")
                list.add(Triple(q, opts, correct))
            }
        } catch (e: Exception) {
            list.add(Triple("What did you learn from this lesson?", listOf("AI learns from patterns", "AI eats pizza", "AI sleeps in bed"), 0))
        }
        list
    }

    val currentQuestion = quizList.firstOrNull()

    Dialog(onDismissRequest = onDismiss) {
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            borderColor = CyanElectric,
            backgroundColor = Color(0xFF0F183D)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!quizStep) {
                    // Story View
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_learning_banner_1784723540177),
                            contentDescription = "Lesson Story",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "📖 Lesson Story",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = lesson.storyText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { quizStep = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("take_quiz_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Take Mini Quiz 🧠", color = SpaceBackground, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Mini Quiz View
                    Text(
                        text = "🧠 Mini Quiz Challenge",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AccentGold
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (currentQuestion != null) {
                        Text(
                            text = currentQuestion.first,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        currentQuestion.second.forEachIndexed { index, option ->
                            val isSelected = (selectedOptionIndex == index)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedOptionIndex = index
                                        isQuizAnsweredCorrectly = (index == currentQuestion.third)
                                    }
                                    .testTag("quiz_option_$index"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) NeonBlue.copy(alpha = 0.3f) else SpaceCardBg
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, CyanElectric) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedOptionIndex = index
                                            isQuizAnsweredCorrectly = (index == currentQuestion.third)
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = CyanElectric)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = option, color = TextPrimary, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        isQuizAnsweredCorrectly?.let { isCorrect ->
                            if (isCorrect) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentGreen.copy(alpha = 0.2f))
                                        .padding(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "Correct", tint = AccentGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Bingo! +${lesson.xpReward} XP Earned! 🎉", color = AccentGreen, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Text(
                                    text = "Oops! Try again to earn XP!",
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onComplete,
                            enabled = isQuizAnsweredCorrectly == true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("claim_rewards_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Claim Rewards & Finish 🏆", color = SpaceBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
