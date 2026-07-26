package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.ChallengeEntity
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

@Composable
fun ChallengesScreen(
    viewModel: InnoKidsViewModel
) {
    val challenges by viewModel.challenges.collectAsState()
    val selectedChallenge by viewModel.selectedChallenge.collectAsState()

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = "Challenges",
                        tint = CyanGlow,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Challenge Arcade 🎮",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
                Text(
                    text = "Play interactive mini games to test your AI powers!",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(challenges) { challenge ->
                ChallengeCardItem(
                    challenge = challenge,
                    onClick = { viewModel.selectChallenge(challenge) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Active Challenge Dialog
        selectedChallenge?.let { challenge ->
            ChallengeInteractiveDialog(
                challenge = challenge,
                onDismiss = { viewModel.selectChallenge(null) },
                onComplete = {
                    viewModel.completeChallenge(challenge.id)
                    viewModel.selectChallenge(null)
                }
            )
        }
    }
}

@Composable
fun ChallengeCardItem(
    challenge: ChallengeEntity,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("challenge_card_${challenge.id}"),
        borderColor = if (challenge.isCompleted) AccentGreen.copy(alpha = 0.5f) else NeonPurple.copy(alpha = 0.5f),
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
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (challenge.isCompleted) AccentGreen.copy(alpha = 0.2f) else NeonPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (challenge.isCompleted) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = AccentGreen, modifier = Modifier.size(28.dp))
                } else {
                    Icon(imageVector = Icons.Default.Extension, contentDescription = "Play", tint = NeonPurple, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "XP", tint = AccentGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${challenge.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall.copy(color = AccentGold, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (challenge.isCompleted) AccentGreen else NeonPurple
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (challenge.isCompleted) "Replay" else "Play",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ChallengeInteractiveDialog(
    challenge: ChallengeEntity,
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            borderColor = NeonPurple,
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
                        text = challenge.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                when (challenge.type) {
                    "SORT_AI" -> SortAiVsHumanGame(onComplete = onComplete)
                    "PATTERN" -> PatternDetectiveGame(onComplete = onComplete)
                    "BUILD_ROBOT" -> BuildRobotGame(onComplete = onComplete)
                    else -> PromptMasterGame(onComplete = onComplete)
                }
            }
        }
    }
}

@Composable
fun SortAiVsHumanGame(onComplete: () -> Unit) {
    val itemsToSort = listOf(
        Pair("Empathy & Kindness", "HUMAN"),
        Pair("100,000 Math Calculations/sec", "AI"),
        Pair("Creative Story Imagination", "HUMAN"),
        Pair("Analyzing 50,000 Cat Photos", "AI")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    if (currentIndex < itemsToSort.size) {
        val currentItem = itemsToSort[currentIndex]
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Classify this card:",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanElectric)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = currentItem.first,
                        style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentItem.second == "HUMAN") score += 1
                        currentIndex += 1
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("❤️ Human Feature", color = TextPrimary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        if (currentItem.second == "AI") score += 1
                        currentIndex += 1
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🤖 AI Feature", color = SpaceBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = AccentGreen, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Challenge Completed! Score: $score / ${itemsToSort.size}", color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Claim +100 XP 🏆", color = SpaceBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PatternDetectiveGame(onComplete: () -> Unit) {
    var answer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Column {
        Text("Find the missing neural pattern value:", color = TextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Pattern: 2 -> 4 -> 8 -> 16 -> [ ? ]", style = MaterialTheme.typography.titleLarge.copy(color = CyanGlow, fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("24", "32", "64").forEach { choice ->
                Button(
                    onClick = {
                        if (choice == "32") isCorrect = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (choice == "32" && isCorrect) AccentGreen else SpaceCardBg
                    )
                ) {
                    Text(choice, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isCorrect) {
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Awesome! Claim +120 XP 🚀", color = SpaceBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BuildRobotGame(onComplete: () -> Unit) {
    val assembledParts = remember { mutableStateListOf<String>() }
    val robotParts = listOf("Vision Camera 👁️", "Neural AI Core 🧠", "Speaker Voice 🔊", "Cyber Armor 🛡️")

    Column {
        Text("Assemble Inno Robot by tapping all 4 core modules:", color = TextSecondary)
        Spacer(modifier = Modifier.height(10.dp))

        robotParts.forEach { part ->
            val isAdded = assembledParts.contains(part)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (!isAdded) assembledParts.add(part)
                    },
                colors = CardDefaults.cardColors(containerColor = if (isAdded) AccentGreen.copy(alpha = 0.2f) else SpaceCardBg)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(part, color = TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (isAdded) Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = AccentGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (assembledParts.size == robotParts.size) {
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inno Assembled! Claim +150 XP 🤖", color = SpaceBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PromptMasterGame(onComplete: () -> Unit) {
    var isDone by remember { mutableStateOf(false) }

    Column {
        Text("Learn to make prompts super descriptive!", color = TextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Weak Prompt: 'Draw a robot.'", color = Color(0xFFEF4444))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Magic Prompt: '3D white and cyan friendly robot smiling in space, 8K render'", color = AccentGreen, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { isDone = true },
            colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cast Magic Prompt! ✨", color = SpaceBackground, fontWeight = FontWeight.Bold)
        }

        if (isDone) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Prompt Master Certified! +200 XP 🏆", color = SpaceBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}
