package com.omarahmed.myassistant.alarmmanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.startAlarm
import com.omarahmed.myassistant.data.CoursesDatabase
import com.omarahmed.myassistant.assignment.AssignmentInfo
import com.omarahmed.myassistant.test.TestInfo
import com.omarahmed.myassistant.data.repository.AssignmentRepository
import com.omarahmed.myassistant.data.repository.TestRepository
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*


class RestartAlarm: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val assignmentDao = CoursesDatabase.getDatabase(applicationContext).assignmentInfoDao()
        val assignmentRepository = AssignmentRepository(assignmentDao)
        observeAssignments(assignmentRepository.getAllAssignment)

        val testDao = CoursesDatabase.getDatabase(applicationContext).testInfoDao()
        val testRepository = TestRepository(testDao)
        observeTests(testRepository.getAllTest)

        return START_STICKY
    }

    private fun observeTests(liveData: LiveData<List<TestInfo>>){
        liveData.observeForever {
            it.forEach { testInfo ->
                testInfo.notificationDate?.let { notifyDay->
                    val testDay = SimpleDateFormat(DATE_PATTERN, Locale.US).format(testInfo.date!!)
                    startAlarm(
                        applicationContext,
                        testInfo.id,
                        notifyDay.time,
                        testInfo.name,
                        testDay,
                        "test"
                    )
                }
            }
        }
    }
    private fun observeAssignments(liveData: LiveData<List<AssignmentInfo>>){
        liveData.observeForever{
            it.forEach { assignmentInfo ->
                assignmentInfo.notificationDate?.let {notifyDay->
                    val deadline = SimpleDateFormat(DATE_PATTERN, Locale.US).format(assignmentInfo.deadLine!!)
                    startAlarm(
                        applicationContext,
                        assignmentInfo.id,
                        notifyDay.time,
                        assignmentInfo.name,
                        deadline,
                        "assignment"
                    )
                }
            }
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        this.stopSelf()
    }

}