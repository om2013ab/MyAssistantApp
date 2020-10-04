package com.omarahmed.myassistant.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holidays_table")
data class HolidayInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val date: Date,
    val locations: String
){
    data class Date(
        val datetime: DateTime
    ){
        data class DateTime(
            val day:Int
        )
    }
}

