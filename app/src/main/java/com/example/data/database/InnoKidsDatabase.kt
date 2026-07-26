package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.InnoKidsDao
import com.example.data.model.BadgeEntity
import com.example.data.model.ChallengeEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.LessonEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        LessonEntity::class,
        ChallengeEntity::class,
        BadgeEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class InnoKidsDatabase : RoomDatabase() {
    abstract fun innoKidsDao(): InnoKidsDao

    companion object {
        @Volatile
        private var INSTANCE: InnoKidsDatabase? = null

        fun getDatabase(context: Context): InnoKidsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InnoKidsDatabase::class.java,
                    "innokids_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
