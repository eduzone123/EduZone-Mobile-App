package com.example.data.repository

import com.example.data.database.Course
import com.example.data.database.CourseDao
import com.example.data.database.UserAccount
import com.example.data.database.UserDao
import com.example.data.database.UserCourseProgress
import com.example.data.database.UserCourseProgressDao
import kotlinx.coroutines.flow.Flow

class EduZoneRepository(
    private val courseDao: CourseDao,
    private val userDao: UserDao,
    private val userCourseProgressDao: UserCourseProgressDao
) {
    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()
    val enrolledCourses: Flow<List<Course>> = courseDao.getEnrolledCourses()

    suspend fun getCourseById(id: Int): Course? = courseDao.getCourseById(id)

    fun getUserProgressFlow(userEmail: String): Flow<List<UserCourseProgress>> {
        return userCourseProgressDao.getProgressByEmail(userEmail)
    }

    suspend fun enrollInCourse(courseId: Int, userEmail: String) {
        val id = "${userEmail}_$courseId"
        val progressRecord = UserCourseProgress(
            id = id,
            userEmail = userEmail,
            courseId = courseId,
            enrolled = true,
            progress = 0
        )
        userCourseProgressDao.insertOrUpdateProgress(progressRecord)
    }

    suspend fun updateCourseProgress(courseId: Int, userEmail: String, progress: Int) {
        val id = "${userEmail}_$courseId"
        val existing = userCourseProgressDao.getProgressForCourse(userEmail, courseId)
        val progressRecord = UserCourseProgress(
            id = id,
            userEmail = userEmail,
            courseId = courseId,
            enrolled = existing?.enrolled ?: true,
            progress = progress
        )
        userCourseProgressDao.insertOrUpdateProgress(progressRecord)
    }

    suspend fun unenrollFromCourse(courseId: Int, userEmail: String) {
        userCourseProgressDao.deleteProgress(userEmail, courseId)
    }

    suspend fun getUserByEmail(email: String): UserAccount? {
        return userDao.getUserByEmail(email)
    }

    suspend fun registerUser(user: UserAccount) {
        userDao.insertUser(user)
    }

    suspend fun populateDefaults() {
        // Populate standard courses
        if (courseDao.getCourseCount() == 0) {
            val courses = listOf(
                Course(
                    title = "Kotlin Basics & Object-Oriented Fundamentals",
                    description = "Learn variables, strict types, null-safety, scopes, classes, and fundamental modern lambdas.",
                    difficulty = "Beginner",
                    language = "Kotlin",
                    duration = "4 hours"
                ),
                Course(
                    title = "Jetpack Compose: Production-Grade UI",
                    description = "Build complex fluid declarative interfaces. Design lists, stateful modifiers, beautiful responsive layouts, and modern Material 3 schemas.",
                    difficulty = "Intermediate",
                    language = "Kotlin",
                    duration = "6 hours"
                ),
                Course(
                    title = "Python Programming Essentials",
                    description = "Build clean scripts, control flows, data collections, and explore packages for automation and file parsing.",
                    difficulty = "Beginner",
                    language = "Python",
                    duration = "5 hours"
                ),
                Course(
                    title = "JavaScript & Contemporary React",
                    description = "Build state-driven web interfaces. Experience modules, hooks, asynchronous calls, and clean virtual structures.",
                    difficulty = "Intermediate",
                    language = "JavaScript",
                    duration = "8 hours"
                ),
                Course(
                    title = "Generative AI Systems & Prompt Engineering",
                    description = "Learn how LLMs process guidelines, execute agent-level instructions, perform system prompt adjustments, and craft structured JSON APIs.",
                    difficulty = "Advanced",
                    language = "AI",
                    duration = "10 hours"
                )
            )
            courseDao.insertCourses(courses)
        }

        // Populate a quick demo login account
        if (userDao.getUserCount() == 0) {
            val demoUser = UserAccount(
                email = "student@eduzone.com",
                fullName = "Alex Mercer",
                passwordHash = "password" // simple plain comparison for local sandbox demo
            )
            userDao.insertUser(demoUser)
        }
    }
}
