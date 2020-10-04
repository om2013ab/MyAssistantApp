package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.omarahmed.myassistant.data.models.CourseInfo

@Dao
interface CourseInfoDao {
    //course info dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(courseInfo: CourseInfo)

    @Query("SELECT * FROM course_info_table ORDER BY courseId")
    fun getAllCourses():LiveData<List<CourseInfo>>

    @Delete
    suspend fun deleteCourse(courseInfo: CourseInfo)

    @Update
    suspend fun updateCourse(courseInfo: CourseInfo)

    @Query("DELETE FROM course_info_table")
    suspend fun deleteAllCourses()


}