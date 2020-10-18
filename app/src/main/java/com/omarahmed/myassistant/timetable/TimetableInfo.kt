package com.omarahmed.myassistant.timetable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "timetable_table")
@Parcelize
data class TimetableInfo(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val from:Date?,
    val to: Date?,
    val venue:String,
    val day:String
): Parcelable