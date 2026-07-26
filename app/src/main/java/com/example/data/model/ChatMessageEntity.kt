package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "INNO"
    val text: String,
    val category: String = "GENERAL", // QUESTION, FUN_FACT, STORY, CHALLENGE
    val timestamp: Long = System.currentTimeMillis()
)
