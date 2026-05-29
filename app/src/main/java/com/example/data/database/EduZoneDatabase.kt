package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Course::class, UserAccount::class, UserCourseProgress::class],
    version = 2,
    exportSchema = false
)
abstract class EduZoneDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun userDao(): UserDao
    abstract fun userCourseProgressDao(): UserCourseProgressDao

    companion object {
        @Volatile
        private var INSTANCE: EduZoneDatabase? = null

        fun getDatabase(context: Context): EduZoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduZoneDatabase::class.java,
                    "eduzone_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
