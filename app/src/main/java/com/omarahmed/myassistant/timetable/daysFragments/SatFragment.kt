package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_sat.*


class SatFragment : BaseDaysFragment("Saturday", R.layout.fragment_sat) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabSaturday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvSaturday.adapter = timetableAdapter
    }


}



