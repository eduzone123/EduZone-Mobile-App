package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCourseProgressDao {
    @Query("SELECT * FROM user_course_progress WHERE userEmail = :userEmail")
    fun getProgressByEmail(userEmail: String): Flow<List<UserCourseProgress>>

    @Query("SELECT * FROM user_course_progress WHERE userEmail = :userEmail AND courseId = :courseId LIMIT 1")
    suspend fun getProgressForCourse(userEmail: String, courseId: Int): UserCourseProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserCourseProgress)

    @Query("DELETE FROM user_course_progress WHERE userEmail = :userEmail AND courseId = :courseId")
    suspend fun deleteProgress(userEmail: String, courseId: Int)
}
