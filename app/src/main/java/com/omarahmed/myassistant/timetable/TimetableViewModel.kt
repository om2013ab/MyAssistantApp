package com.omarahmed.myassistant.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.data.models.TimetableInfo
import com.omarahmed.myassistant.data.repository.TimetableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimetableViewModel(application: Application): AndroidViewModel(application) {
    private val dao = CoursesDatabase.getDatabase(application).timetableDao()
    private val repository: TimetableRepository

    val getScheduleToCheckEmpty:LiveData<List<TimetableInfo>>

    private val _emptyTimetable = MutableLiveData<Boolean>(false)
    val emptyTimetable: LiveData<Boolean>
        get() = _emptyTimetable

    init {
        repository = TimetableRepository(dao)
        getScheduleToCheckEmpty = repository.getScheduleToCheckEmpty
    }

    fun getSchedule(scheduleDay:String):LiveData<List<TimetableInfo>>{
        return repository.getAllSchedules(scheduleDay)
    }

    fun insertSchedules(timetableInfo: TimetableInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSchedule(timetableInfo)
        }
    }

    fun deleteAllSchedules(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun deleteSchedule(timetableInfo: TimetableInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(timetableInfo)
        }
    }

    fun updateSchedule(timetableInfo: TimetableInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSchedule(timetableInfo)
        }
    }


    fun checkEmptyTimetable(timetableInfo:List<TimetableInfo>){
        _emptyTimetable.value = timetableInfo.isEmpty()
    }
}