package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_tues.*


class TuesFragment : BaseDaysFragment("Tuesday", R.layout.fragment_tues) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabTuesday.setOnClickListener {
            setupAddScheduleDialog()
        }

        rvTuesday.adapter = timetableAdapter
    }

}