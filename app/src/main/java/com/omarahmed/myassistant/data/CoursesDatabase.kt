package com.omarahmed.myassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omarahmed.myassistant.data.dao.*
import com.omarahmed.myassistant.data.models.*
import com.omarahmed.myassistant.data.remote.CountriesResponse

@Database(
    entities = [CourseInfo::class, AssignmentInfo::class,
        TestInfo::class, TimetableInfo::class, CountriesResponse.Countries.CountriesInfo::class
    ,HolidayInfo::class],
    version = 16,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CoursesDatabase : RoomDatabase() {
    abstract fun courseInfoDao(): CourseInfoDao
    abstract fun assignmentInfoDao(): AssignmentInfoDao
    abstract fun testInfoDao(): TestInfoDao
    abstract fun timetableDao():TimetableDao
    abstract fun countriesDao(): CountriesDao
    abstract fun holidayDao(): HolidayDao

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