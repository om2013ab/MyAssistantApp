package com.omarahmed.myassistant.data.repository

import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.data.dao.CourseInfoDao
import com.omarahmed.myassistant.data.models.CourseInfo

class HomeRepository(private val dao: CourseInfoDao){
    val getAllCourses: LiveData<List<CourseInfo>>  = dao.getAllCourses()

    suspend fun insertCourse(courseInfo: CourseInfo){
        dao.insertCourse(courseInfo)
    }

    suspend fun deleteCourse(courseInfo: CourseInfo){
        dao.deleteCourse(courseInfo)
    }

    suspend fun updateCourse(courseInfo: CourseInfo){
        dao.updateCourse(courseInfo)
    }

    suspend fun deleteAllCourses(){
        dao.deleteAllCourses()
    }

}

