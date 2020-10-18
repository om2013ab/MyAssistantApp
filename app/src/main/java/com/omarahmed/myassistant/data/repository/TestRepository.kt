package com.omarahmed.myassistant.data.repository

import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.data.dao.TestInfoDao
import com.omarahmed.myassistant.test.TestInfo

class TestRepository(val dao: TestInfoDao) {
    val getAllTest:LiveData<List<TestInfo>> = dao.getAllTests()

    suspend fun insertTest(testInfo: TestInfo){
        dao.insertTest(testInfo)
    }

    suspend fun updateTest(testInfo: TestInfo){
        dao.updateTest(testInfo)
    }

    suspend fun deleteTest(testInfo: TestInfo){
        dao.deleteTest(testInfo)
    }

    suspend fun deleteAllTest(){
        dao.deleteAllTests()
    }
}