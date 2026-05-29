package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_course_progress")
data class UserCourseProgress(
    @PrimaryKey
    val id: String, // Constructed as "userEmail_courseId"
    val userEmail: String,
    val courseId: Int,
    val enrolled: Boolean = false,
    val progress: Int = 0
)
