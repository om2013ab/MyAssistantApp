package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_thur.*


class ThurFragment : BaseDaysFragment("Thursday",R.layout.fragment_thur) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabThursday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvThursday.adapter = timetableAdapter
    }
}