package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.omarahmed.myassistant.data.models.TimetableInfo


@Dao
interface TimetableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(timetableInfo: TimetableInfo)

    @Query("SELECT * FROM timetable_table WHERE day LIKE:scheduleDay ORDER BY `from`")
    fun getAllSchedules(scheduleDay:String):LiveData<List<TimetableInfo>>

    @Query("DELETE FROM timetable_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteSchedule(timetableInfo: TimetableInfo)

    @Update
    suspend fun updateSchedule(timetableInfo: TimetableInfo)

    @Query("SELECT * FROM timetable_table")
    fun getScheduleToCheckEmpty():LiveData<List<TimetableInfo>>
}