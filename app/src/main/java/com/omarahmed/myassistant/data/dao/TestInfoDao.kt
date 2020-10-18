package com.omarahmed.myassistant.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.omarahmed.myassistant.test.TestInfo

@Dao
interface TestInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(testInfo: TestInfo)

    @Query("SELECT * FROM test_table ORDER BY date")
    fun getAllTests(): LiveData<List<TestInfo>>

    @Update
    suspend fun updateTest(testInfo: TestInfo)

    @Delete
    suspend fun deleteTest(testInfo: TestInfo)

    @Query("DELETE FROM test_table")
    suspend fun deleteAllTests()
}