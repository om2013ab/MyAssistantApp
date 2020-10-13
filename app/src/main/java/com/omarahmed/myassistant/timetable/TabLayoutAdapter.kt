package com.omarahmed.myassistant.timetable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.omarahmed.myassistant.timetable.daysFragments.*

class TabLayoutAdapter(fm: FragmentManager): FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MonFragment()
            1 -> TuesFragment()
            2 -> WedFragment()
            3 -> ThurFragment()
            else -> FriFragment()
        }
    }

    override fun getCount() = 5

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "Mon"
            1-> "Tues"
            2-> "Wed"
            3-> "Thur"
            else-> "Fri"
        }
    }
}