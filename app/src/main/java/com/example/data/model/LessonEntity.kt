package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // Beginner, Intermediate, Advanced
    val description: String,
    val storyText: String,
    val estMinutes: Int,
    val isCompleted: Boolean = false,
    val xpReward: Int = 50,
    val coinReward: Int = 20,
    val quizQuestionsJson: String, // Serialized list of quiz questions
    val iconName: String = "psychology"
)
