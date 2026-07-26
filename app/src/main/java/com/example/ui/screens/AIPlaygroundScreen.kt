package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.api.GeminiClient
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.CyanElectric
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.InnoKidsViewModel
import kotlinx.coroutines.launch

@Composable
fun AIPlaygroundScreen(
    viewModel: InnoKidsViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Story Creator 📖", "Prompt Art Studio 🎨")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Playground",
                    tint = CyanGlow,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Playground 🎨",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }
            Text(
                text = "Safely experiment with AI storytelling and art prompts!",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SpaceCardBg,
                contentColor = CyanElectric,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyanElectric
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CyanElectric else TextSecondary
                            )
                        },
                        modifier = Modifier.testTag("playground_tab_$index")
                    )
                }
            }
        }

        if (selectedTab == 0) {
            StoryCreatorTab(viewModel = viewModel)
        } else {
            PromptArtStudioTab()
        }
    }
}

@Composable
fun StoryCreatorTab(viewModel: InnoKidsViewModel) {
    var selectedTopic by remember { mutableStateOf("Space Robot") }
    var characterName by remember { mutableStateOf("Zoggy") }
    var generatedStory by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val topics = listOf("Space Robot", "Superhero Cat", "Magic Jungle", "Underwater AI")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text("1. Choose a Story Theme:", color = TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                topics.take(2).forEach { topic ->
                    val isSelected = selectedTopic == topic
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CyanElectric else SpaceCardBg)
                            .clickable { selectedTopic = topic }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topic,
                            color = if (isSelected) SpaceBackground else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                topics.drop(2).forEach { topic ->
                    val isSelected = selectedTopic == topic
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CyanElectric else SpaceCardBg)
                            .clickable { selectedTopic = topic }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topic,
                            color = if (isSelected) SpaceBackground else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("2. Name Your Main Hero:", color = TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = characterName,
                onValueChange = { characterName = it },
                placeholder = { Text("e.g., Zoggy the Robot", color = TextSecondary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_name_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanElectric,
                    unfocusedBorderColor = Color(0xFF20326B),
                    focusedContainerColor = SpaceCardBg,
                    unfocusedContainerColor = SpaceCardBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    isGenerating = true
                    coroutineScope.launch {
                        val prompt = "Generate a fun 4-sentence bedtime story about a $selectedTopic named $characterName who uses friendly AI to solve a galactic mystery! Include emojis!"
                        generatedStory = GeminiClient.askInno(prompt)
                        isGenerating = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("generate_story_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = SpaceBackground)
                } else {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = "Story", tint = SpaceBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate AI Story ✨", color = SpaceBackground, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (generatedStory.isNotEmpty()) {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyanGlow
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📖 Inno's Story for You:", style = MaterialTheme.typography.titleMedium.copy(color = CyanGlow, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = generatedStory, color = TextPrimary, lineHeight = 22.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun PromptArtStudioTab() {
    var subject by remember { mutableStateOf("A cute robot playing guitar") }
    var selectedStyle by remember { mutableStateOf("3D Pixar Render") }
    var isGenerated by remember { mutableStateOf(true) }

    val styles = listOf("3D Pixar Render", "Cyberpunk Neon", "Pixel Art", "Watercolor")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text("Describe what you want to create:", color = TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("art_subject_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanElectric,
                    unfocusedBorderColor = Color(0xFF20326B),
                    focusedContainerColor = SpaceCardBg,
                    unfocusedContainerColor = SpaceCardBg,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Select AI Art Style:", color = TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                styles.take(2).forEach { style ->
                    val isSelected = selectedStyle == style
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonPurple else SpaceCardBg)
                            .clickable { selectedStyle = style }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyanElectric
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_ai_playground_1784723562275),
                            contentDescription = "AI Art Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Generated Prompt:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text("\"$subject, $selectedStyle style, vibrant lighting, highly detailed\"", style = MaterialTheme.typography.bodySmall.copy(color = CyanGlow, fontWeight = FontWeight.Bold))
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
