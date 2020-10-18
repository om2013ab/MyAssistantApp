package com.omarahmed.myassistant.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.dao.CourseInfoDao
import com.omarahmed.myassistant.data.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dao: CourseInfoDao = CoursesDatabase.getDatabase(application).courseInfoDao()
    private val homeRepository: HomeRepository

    val getAllCourses:LiveData<List<CourseInfo>>

    private val _showDataInBottomSheet = MutableLiveData<CourseInfo>()
    val showDataInBottomSheet: LiveData<CourseInfo>
        get() = _showDataInBottomSheet

    private val _noCourses = MutableLiveData(false)
    val noCourses: LiveData<Boolean>
        get() = _noCourses

    init {
        homeRepository = HomeRepository(dao)
        getAllCourses = homeRepository.getAllCourses
    }



    fun insertCourse(courseInfo: CourseInfo){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.insertCourse(courseInfo)
        }
    }

    fun deleteCourse(courseInfo: CourseInfo){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteCourse(courseInfo)
        }
    }

    fun updateCourse(courseInfo: CourseInfo){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.updateCourse(courseInfo)
        }
    }

    fun deleteAllCourses(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteAllCourses()
        }
    }

    fun bottomSheetInfo(courseInfo: CourseInfo){
        _showDataInBottomSheet.value = courseInfo
    }

    fun checkCoursesEmpty(courseInfo: List<CourseInfo>){
        _noCourses.value = courseInfo.isEmpty()
    }

}