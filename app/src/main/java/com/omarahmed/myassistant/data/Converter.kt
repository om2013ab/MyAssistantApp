package com.omarahmed.myassistant.data

import androidx.room.TypeConverter
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
}
