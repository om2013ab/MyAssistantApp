package com.omarahmed.myassistant.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.models.TestInfo
import com.omarahmed.myassistant.data.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestViewModel(application: Application):AndroidViewModel(application) {
    private val dao = CoursesDatabase.getDatabase(application).testInfoDao()
    private val repository:TestRepository

    val getAllTests:LiveData<List<TestInfo>>

    private val _noTest = MutableLiveData(false)
    val noTest: LiveData<Boolean>
        get() = _noTest

    init {
        repository = TestRepository(dao)
        getAllTests = repository.getAllTest
    }

    fun insertTest(testInfo: TestInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTest(testInfo)
        }
    }

    fun updateTest(testInfo: TestInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTest(testInfo)
        }
    }

    fun deleteTest(testInfo: TestInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTest(testInfo)
        }
    }

    fun deleteAllTests(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTest()
        }
    }
    fun checkTestEmpty(testInfo: List<TestInfo>){
        _noTest.value = testInfo.isEmpty()
    }
}