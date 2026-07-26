package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CyanElectric
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.InnoKidsViewModel

@Composable
fun ParentDashboardScreen(
    viewModel: InnoKidsViewModel
) {
    val user by viewModel.userProfile.collectAsState()
    val isUnlocked by viewModel.isParentUnlocked.collectAsState()
    val pinError by viewModel.parentPinError.collectAsState()

    var pinInput by remember { mutableStateOf("") }
    var selectedScreenLimit by remember { mutableIntStateOf(user?.maxDailyMinutes ?: 60) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBackground)
    ) {
        if (!isUnlocked) {
            // Locked View requiring PIN
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Parent Gate",
                        tint = CyanElectric,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Parent Dashboard",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Text(
                    text = "Enter your 4-digit PIN to access learning statistics and controls. (Default: 1234)",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4) pinInput = it },
                    placeholder = { Text("Enter PIN (1234)", color = TextSecondary) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_pin_input"),
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

                pinError?.let { err ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = err, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.unlockParentDashboard(pinInput) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("unlock_parent_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanElectric),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.LockOpen, contentDescription = "Unlock", tint = SpaceBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unlock Dashboard", color = SpaceBackground, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Unlocked Parent Controls
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Parent Controls 🛡️",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Monitoring learning for ${user?.name ?: "Omar"}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Button(
                            onClick = { viewModel.lockParentDashboard() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", tint = TextPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Lock", color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily Screen Time Card
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = CyanElectric.copy(alpha = 0.4f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Timer, contentDescription = "Time", tint = CyanGlow)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Daily Screen Time Tracker", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val spent = user?.dailyMinutesSpent ?: 25
                            val maxMin = user?.maxDailyMinutes ?: 60
                            val ratio = (spent.toFloat() / maxMin.toFloat()).coerceIn(0f, 1f)

                            Text(
                                text = "$spent mins used of $maxMin mins daily limit",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = CyanElectric,
                                trackColor = SpaceCardBg
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text("Set Daily Limit:", style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(30, 45, 60, 90).forEach { limit ->
                                    val isSel = (selectedScreenLimit == limit)
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                selectedScreenLimit = limit
                                                viewModel.updateParentPinAndLimit(user?.parentPin ?: "1234", limit)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = if (isSel) CyanElectric else SpaceCardBg)
                                    ) {
                                        Box(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            Text("${limit}m", color = if (isSel) SpaceBackground else TextPrimary, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Weekly Progress Breakdown Card
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NeonBlue
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Assessment, contentDescription = "Report", tint = AccentGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Weekly AI Learning Report", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("• Completed Lessons: 4 / 7", color = TextPrimary)
                            Text("• Quiz Accuracy Rate: 92%", color = AccentGreen, fontWeight = FontWeight.Bold)
                            Text("• Strong Areas: AI Concepts, Pattern Recognition", color = CyanGlow)
                            Text("• Areas to Practice: Neural Network Layers", color = AccentGold)

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { /* Export Summary */ },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Export Weekly Report (PDF)", color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
