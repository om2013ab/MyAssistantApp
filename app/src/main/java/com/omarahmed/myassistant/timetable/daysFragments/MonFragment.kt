package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_mon.*


class MonFragment : BaseDaysFragment("Monday", R.layout.fragment_mon) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabMonday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvMonday.adapter = timetableAdapter
    }

}