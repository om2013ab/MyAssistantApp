package com.omarahmed.myassistant.timetable

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.timetable.daysFragments.*
import kotlinx.android.synthetic.main.fragment_timetable.*

class TimetableFragment : Fragment(R.layout.fragment_timetable){
    private val timetableViewModel: TimetableViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = arrayListOf(
            MonFragment(),
            TuesFragment(),
            WedFragment(),
            ThurFragment(),
            FriFragment(),
            SatFragment(),
            SunFragment()
        )
        val tabLayoutAdapter = TabLayoutAdapter(fragments,requireActivity().supportFragmentManager,lifecycle)
        viewPager.adapter = tabLayoutAdapter

        TabLayoutMediator(tabLayout,viewPager){tab,position ->
            when(position){
                0 -> tab.text = "Mon"
                1 -> tab.text = "Tues"
                2 -> tab.text = "Wed"
                3 -> tab.text = "Thur"
                4 -> tab.text = "Fri"
                5 -> tab.text = "Sat"
                else -> tab.text = "Sun"
            }
        }.attach()

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbarTimetable)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll && timetableViewModel.emptyTimetable.value == false) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete all")
                .setMessage("Are you sure you want to delete all schedules ?")
                .setPositiveButton("Yes") { _, _ ->
                    timetableViewModel.deleteAllSchedules()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
        else{
            Toast.makeText(requireContext(),"You do not have any schedule to delete",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}