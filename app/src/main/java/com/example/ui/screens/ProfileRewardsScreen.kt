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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.BadgeEntity
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
fun ProfileRewardsScreen(
    viewModel: InnoKidsViewModel
) {
    val user by viewModel.userProfile.collectAsState()
    val badges by viewModel.badges.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showCertificateDialog by remember { mutableStateOf(false) }

    val avatars = listOf("Cyber Hero", "Neon Explorer", "Space Pilot", "Astro Genius")
    val robotSkins = listOf("Inno Classic", "Cyber Neon Inno", "Gold Master Inno")

    Column(
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "${user?.name ?: "Omar"}'s Rewards 🏆",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Level ${user?.level ?: 3} • ${user?.xp ?: 340} XP",
                            style = MaterialTheme.typography.bodyMedium.copy(color = CyanGlow)
                        )
                    }

                    Button(
                        onClick = { showCertificateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("view_certificate_button")
                    ) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = "Certificate", tint = SpaceBackground)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Certificate", color = SpaceBackground, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Customization Section: Avatars
                Text("Customize Avatar:", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    avatars.take(2).forEach { av ->
                        val isSelected = (user?.selectedAvatar == av)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonBlue else SpaceCardBg)
                                .clickable { viewModel.updateAvatarAndRobot(av, user?.selectedRobot ?: "Inno Classic") }
                                .padding(12.dp)
                                .testTag("avatar_$av"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(av, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    avatars.drop(2).forEach { av ->
                        val isSelected = (user?.selectedAvatar == av)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonBlue else SpaceCardBg)
                                .clickable { viewModel.updateAvatarAndRobot(av, user?.selectedRobot ?: "Inno Classic") }
                                .padding(12.dp)
                                .testTag("avatar_$av"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(av, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Robot Companion Skin
                Text("Robot Companion Skin:", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    robotSkins.forEach { rSkin ->
                        val isSelected = (user?.selectedRobot == rSkin)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CyanElectric else SpaceCardBg)
                                .clickable { viewModel.updateAvatarAndRobot(user?.selectedAvatar ?: "Cyber Hero", rSkin) }
                                .padding(10.dp)
                                .testTag("robot_$rSkin"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(rSkin, color = if (isSelected) SpaceBackground else TextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Badges Gallery
                Text("Achievements & Badges 🏅", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(badges) { badge ->
                BadgeItem(badge = badge)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Certificate Dialog
        if (showCertificateDialog) {
            Dialog(onDismissRequest = { showCertificateDialog = false }) {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    borderColor = AccentGold,
                    backgroundColor = Color(0xFF0F183D)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_certificate_badge_1784723550521),
                            contentDescription = "Certificate Medal",
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("INNOKIDS AI ACADEMY", style = MaterialTheme.typography.labelMedium.copy(color = AccentGold, fontWeight = FontWeight.Bold))
                        Text("CERTIFICATE OF EXCELLENCE", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.ExtraBold))

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("This certifies that", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text("${user?.name ?: "Omar"}", style = MaterialTheme.typography.headlineSmall.copy(color = CyanElectric, fontWeight = FontWeight.Bold))
                        Text("has successfully mastered Introduction to AI & Prompt Engineering!", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary), fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showCertificateDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Download / Close 🎓", color = SpaceBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItem(badge: BadgeEntity) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (badge.isUnlocked) AccentGold.copy(alpha = 0.5f) else Color(0xFF334155)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (badge.isUnlocked) AccentGold.copy(alpha = 0.2f) else Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = badge.title,
                    tint = if (badge.isUnlocked) AccentGold else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (badge.isUnlocked) TextPrimary else TextSecondary
                    )
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            if (badge.isUnlocked) {
                Text("Unlocked", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            } else {
                Text("Locked 🔒", color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}
