package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // SORT_AI, PATTERN, BUILD_ROBOT, PROMPT
    val description: String,
    val xpReward: Int,
    val isCompleted: Boolean = false
)
