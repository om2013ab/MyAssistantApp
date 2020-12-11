package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_wed.*


class WedFragment : BaseDaysFragment("Wednesday", R.layout.fragment_wed) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabWednesday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvWednesday.adapter = timetableAdapter
    }


}