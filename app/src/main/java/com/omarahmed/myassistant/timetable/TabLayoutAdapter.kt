package com.omarahmed.myassistant.timetable

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.omarahmed.myassistant.timetable.daysFragments.BaseDaysFragment

class TabLayoutAdapter(
    private val fragments: ArrayList<BaseDaysFragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]

}