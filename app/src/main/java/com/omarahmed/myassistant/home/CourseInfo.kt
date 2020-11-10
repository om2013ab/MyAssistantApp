package com.omarahmed.myassistant.home

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "course_info_table")
@Parcelize
data class CourseInfo(
    @PrimaryKey(autoGenerate = true)
    val courseId:Int,
    val courseName:String,
    val courseCode:String,
    val courseHours:String,
    val courseLecturer:String
):Parcelable