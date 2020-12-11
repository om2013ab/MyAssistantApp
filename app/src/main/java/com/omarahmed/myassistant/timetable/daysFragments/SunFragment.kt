package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_sun.*


class SunFragment : BaseDaysFragment("Sunday", R.layout.fragment_sun) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabSunday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvSunday.adapter = timetableAdapter
    }


}