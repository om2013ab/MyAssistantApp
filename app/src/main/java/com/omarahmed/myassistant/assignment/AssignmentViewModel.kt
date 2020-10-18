package com.omarahmed.myassistant.assignment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.repository.AssignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AssignmentViewModel(application:Application): AndroidViewModel(application) {

    private val dao = CoursesDatabase.getDatabase(application).assignmentInfoDao()
    private val repository: AssignmentRepository

    val getAllAssignment:LiveData<List<AssignmentInfo>>

    private val _noAssignment = MutableLiveData(false)
    val noAssignment:LiveData<Boolean>
        get() = _noAssignment

    init {
        repository = AssignmentRepository(dao)
        getAllAssignment = repository.getAllAssignment
    }

    fun insertAssignment(assignmentInfo: AssignmentInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAssignment(assignmentInfo)
        }
    }

    fun deleteAssignment(assignmentInfo: AssignmentInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAssignment(assignmentInfo)
        }
    }

    fun updateAssignment(assignmentInfo: AssignmentInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAssignment(assignmentInfo)
        }
    }

    fun deleteAllAssignments(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAssignments()
        }
    }
    fun checkDatabaseEmpty(assignmentInfo: List<AssignmentInfo>){
        _noAssignment.value = assignmentInfo.isEmpty()
    }
}