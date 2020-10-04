package com.omarahmed.myassistant.utils

import android.text.Editable
import android.text.TextWatcher
import com.omarahmed.myassistant.databinding.DialogAddAssignmentBinding
import com.omarahmed.myassistant.databinding.DialogAddCourseBinding
import com.omarahmed.myassistant.databinding.DialogAddSchedualBinding
import com.omarahmed.myassistant.databinding.DialogAddTestBinding


abstract class TextWatcher : TextWatcher {

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    companion object{
        fun textChangedHome(view: DialogAddCourseBinding){
            val onTextChanged = object : com.omarahmed.myassistant.utils.TextWatcher() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    view.btnAddCourse.isEnabled = view.courseName.text.isNotEmpty()
                            && view.courseCode.text.isNotEmpty()
                            && view.creditHours.text.isNotEmpty()
                }
            }
            view.apply {
                courseName.addTextChangedListener(onTextChanged)
                courseCode.addTextChangedListener(onTextChanged)
                creditHours.addTextChangedListener(onTextChanged)
            }

        }
        fun textChangedAssignment(view:DialogAddAssignmentBinding){
            val onTextChanged = object : com.omarahmed.myassistant.utils.TextWatcher() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    view.btnAdd.isEnabled = view.codeAssignment.text.isNotEmpty()
                            && view.deadline.text!!.isNotEmpty()
                            && view.description.text!!.isNotEmpty()
                }
            }
            view.apply {
                codeAssignment.addTextChangedListener(onTextChanged)
                deadline.addTextChangedListener(onTextChanged)
                description.addTextChangedListener(onTextChanged)
            }
        }
        fun textChangedTest(view: DialogAddTestBinding){
            val onTextChanged = object : com.omarahmed.myassistant.utils.TextWatcher() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    view.btnAdd.isEnabled = view.codeTest.text.isNotEmpty()
                            && view.dateTest.text!!.isNotEmpty()
                            && view.timeTest.text!!.isNotEmpty()
                }
            }
            view.apply {
                codeTest.addTextChangedListener(onTextChanged)
                dateTest.addTextChangedListener(onTextChanged)
                timeTest.addTextChangedListener(onTextChanged)
            }
        }
        fun textChangedTimetable(view: DialogAddSchedualBinding){
            val onTextChanged = object : com.omarahmed.myassistant.utils.TextWatcher() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    view.btnAdd.isEnabled = view.code.text.isNotEmpty()
                            && view.from.text!!.isNotEmpty()
                            && view.to.text!!.isNotEmpty()
                }
            }
            view.apply {
                code.addTextChangedListener(onTextChanged)
                from.addTextChangedListener(onTextChanged)
                to.addTextChangedListener(onTextChanged)
            }
        }

    }

}

