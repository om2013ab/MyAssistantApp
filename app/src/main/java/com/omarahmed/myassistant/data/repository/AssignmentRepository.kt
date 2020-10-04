package com.omarahmed.myassistant.data.repository

import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.data.dao.AssignmentInfoDao
import com.omarahmed.myassistant.data.models.AssignmentInfo

class AssignmentRepository(private val dao: AssignmentInfoDao) {
    val getAllAssignment: LiveData<List<AssignmentInfo>> = dao.getAllAssignments()

    suspend fun insertAssignment(assignmentInfo: AssignmentInfo){
        dao.insertAssignment(assignmentInfo)
    }

    suspend fun deleteAssignment(assignmentInfo: AssignmentInfo){
        dao.deleteAssignment(assignmentInfo)
    }

    suspend fun updateAssignment(assignmentInfo: AssignmentInfo){
        dao.updateAssignment(assignmentInfo)
    }

    suspend fun deleteAllAssignments(){
        dao.deleteAllAssignments()
    }
}