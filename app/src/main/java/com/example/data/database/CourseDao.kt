package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE enrolled = 1 ORDER BY id ASC")
    fun getEnrolledCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: Int): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Query("UPDATE courses SET enrolled = 1, enrolledUserEmail = :userEmail WHERE id = :courseId")
    suspend fun enrollInCourse(courseId: Int, userEmail: String)

    @Query("UPDATE courses SET progress = :progress WHERE id = :courseId")
    suspend fun updateProgress(courseId: Int, progress: Int)

    @Query("UPDATE courses SET enrolled = 0, progress = 0, enrolledUserEmail = null WHERE id = :courseId")
    suspend fun unenroll(courseId: Int)

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun getCourseCount(): Int
}
