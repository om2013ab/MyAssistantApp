package com.omarahmed.myassistant.home

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "course_info_table")
data class CourseInfo(
    @PrimaryKey(autoGenerate = true)
    val courseId:Int,
    val courseName:String,
    val courseCode:String,
    val courseHours:String,
    val courseLecturer:String
)