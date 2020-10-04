package com.omarahmed.myassistant.data.repository

import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.data.dao.TimetableDao
import com.omarahmed.myassistant.data.models.TimetableInfo

class TimetableRepository(private val dao: TimetableDao) {

    val getScheduleToCheckEmpty:LiveData<List<TimetableInfo>> = dao.getScheduleToCheckEmpty()

    fun getAllSchedules(scheduleDay:String):LiveData<List<TimetableInfo>> = dao.getAllSchedules(scheduleDay)

    suspend fun insertSchedule(timetableInfo: TimetableInfo){
        dao.insertSchedule(timetableInfo)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

    suspend fun deleteSchedule(timetableInfo: TimetableInfo){
        dao.deleteSchedule(timetableInfo)
    }

    suspend fun updateSchedule(timetableInfo: TimetableInfo){
        dao.updateSchedule(timetableInfo)
    }


}