package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omarahmed.myassistant.data.models.HolidayInfo

@Dao
interface HolidayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolidays(holidayInfo: HolidayInfo)

    @Query("SELECT * FROM holidays_table ORDER BY date")
    fun getHolidays():LiveData<List<HolidayInfo>>
}