package com.omarahmed.myassistant.data

import androidx.room.TypeConverter
import com.omarahmed.myassistant.holiday.HolidayInfo
import java.util.*

class Converter {

    @TypeConverter
    fun toDate(date: Long?): Date? {
        return date?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDateTime(dayTime: HolidayInfo.Date): HolidayInfo.Date.DateTime{
        return dayTime.datetime
    }

    @TypeConverter
    fun fromDateTime(dayTime: HolidayInfo.Date.DateTime): HolidayInfo.Date{
        return HolidayInfo.Date(dayTime)
    }

    @TypeConverter
    fun toInt(day: HolidayInfo.Date.DateTime): Int {
        return day.day
    }

    @TypeConverter
    fun fromInt(day: Int): HolidayInfo.Date.DateTime{
        return HolidayInfo.Date.DateTime(day)
    }
}
