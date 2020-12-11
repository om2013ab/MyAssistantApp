package com.omarahmed.myassistant.timetable.daysFragments

import android.os.Bundle
import android.view.View
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.fragment_fri.*

class FriFragment : BaseDaysFragment(
    "Friday",
    R.layout.fragment_fri
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabFriday.setOnClickListener {
            setupAddScheduleDialog()
        }
        rvFriday.adapter = timetableAdapter
    }

}