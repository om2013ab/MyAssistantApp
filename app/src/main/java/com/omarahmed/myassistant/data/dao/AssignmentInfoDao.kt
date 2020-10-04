package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.omarahmed.myassistant.data.models.AssignmentInfo

@Dao
interface AssignmentInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignmentInfo: AssignmentInfo)

    @Query("SELECT * FROM assignments_table ORDER BY deadLine")
    fun getAllAssignments(): LiveData<List<AssignmentInfo>>

    @Delete
    suspend fun deleteAssignment(assignmentInfo: AssignmentInfo)

    @Update
    suspend fun updateAssignment(assignmentInfo: AssignmentInfo)

    @Query("DELETE FROM assignments_table")
    suspend fun deleteAllAssignments()
}