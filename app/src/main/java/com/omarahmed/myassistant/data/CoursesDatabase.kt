package com.omarahmed.myassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omarahmed.myassistant.assignment.AssignmentInfo
import com.omarahmed.myassistant.data.dao.AssignmentInfoDao
import com.omarahmed.myassistant.data.dao.CourseInfoDao
import com.omarahmed.myassistant.data.dao.TestInfoDao
import com.omarahmed.myassistant.data.dao.TimetableDao
import com.omarahmed.myassistant.home.CourseInfo
import com.omarahmed.myassistant.test.TestInfo
import com.omarahmed.myassistant.timetable.TimetableInfo

@Database(
    entities = [CourseInfo::class, AssignmentInfo::class, TestInfo::class, TimetableInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CoursesDatabase : RoomDatabase() {
    abstract fun courseInfoDao(): CourseInfoDao
    abstract fun assignmentInfoDao(): AssignmentInfoDao
    abstract fun testInfoDao(): TestInfoDao
    abstract fun timetableDao(): TimetableDao
    companion object {
        @Volatile
        private var INSTANCE: CoursesDatabase? = null

        fun getDatabase(context: Context): CoursesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    ).also {
                        INSTANCE = it
                    }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoursesDatabase::class.java,
                "my_assistant_database"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}