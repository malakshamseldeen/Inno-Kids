package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Omar",
    val age: Int = 9,
    val level: Int = 3,
    val xp: Int = 340,
    val xpForNextLevel: Int = 500,
    val coins: Int = 120,
    val streakDays: Int = 5,
    val selectedAvatar: String = "Cyber Hero",
    val selectedRobot: String = "Inno Classic",
    val isParentPinSet: Boolean = true,
    val parentPin: String = "1234",
    val dailyMinutesSpent: Int = 25,
    val maxDailyMinutes: Int = 60
)
