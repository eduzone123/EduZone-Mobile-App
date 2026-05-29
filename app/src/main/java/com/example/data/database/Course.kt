package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val language: String,   // "Kotlin", "Python", "JavaScript", "AI"
    val duration: String,   // e.g., "4 hours", "8 hours"
    val enrolled: Boolean = false,
    val progress: Int = 0, // 0 to 100
    val enrolledUserEmail: String? = null // Associated with logged in user
)
