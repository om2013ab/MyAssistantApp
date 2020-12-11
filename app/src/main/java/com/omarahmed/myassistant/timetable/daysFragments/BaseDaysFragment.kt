package com.omarahmed.myassistant.timetable.daysFragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.databinding.DialogAddSchedualBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.timetable.TimetableAdapter
import com.omarahmed.myassistant.timetable.TimetableFragmentDirections
import com.omarahmed.myassistant.timetable.TimetableInfo
import com.omarahmed.myassistant.timetable.TimetableViewModel
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.TextWatcher
import com.omarahmed.myassistant.utils.TimePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseDaysFragment(
    private val day: String,
    layout: Int
): Fragment(layout) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val timetableViewModel: TimetableViewModel by viewModels()
    protected lateinit var timetableAdapter: TimetableAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timetableAdapter = TimetableAdapter(TimetableAdapter.OnClickListener{ timetableInfo, view ->
            setupPopMenu(timetableInfo,view)
        })
        timetableViewModel.getSchedule(day).observe(viewLifecycleOwner, {
            timetableAdapter.scheduleList.submitList(it)
        })

    }
    private fun setupPopMenu(currentSchedule: TimetableInfo, view: View) {
        val popupMenu = PopupMenu(requireContext(),view)
        popupMenu.apply {
            menuInflater.inflate(R.menu.options_menu,popupMenu.menu)
            show()
            setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.edit ->{
                        val action = TimetableFragmentDirections.actionTimetableFragmentToTimetableUpdateFragment(currentSchedule)
                        findNavController().navigate(action)
                    }
                    R.id.delete ->{
                        timetableViewModel.deleteSchedule(currentSchedule)
                        Snackbar.make(requireView(),"Successfully deleted", Snackbar.LENGTH_LONG).apply {
                            show()
                            setAction("UNDO"){
                                timetableViewModel.insertSchedules(currentSchedule)
                            }
                        }
                    }
                }
                false
            }
        }
    }

    protected fun setupAddScheduleDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = DialogAddSchedualBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(view.root)
        val dialog = builder.create()
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
        setupDropDownMenu(view)
        view.btnCancel.setOnClickListener { dialog.dismiss() }
        view.from.setOnClickListener { TimePicker.timePickerDialog(view.from,requireContext())}
        view.to.setOnClickListener { TimePicker.timePickerDialog(view.to,requireContext())}
        view.btnAdd.setOnClickListener { insertSchedule(view,dialog) }
        TextWatcher.textChangedTimetable(view)
    }

    private fun setupDropDownMenu(view: DialogAddSchedualBinding) {
        val nameList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for (code in it){
                nameList.add(code.courseName)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout,nameList)
            view.name.setAdapter(adapter)
        })
    }

    private fun insertSchedule(view: DialogAddSchedualBinding, dialog: AlertDialog) {
        val name = view.name.text.toString()
        val from = SimpleDateFormat(Constants.TIME_PATTERN, Locale.US).parse(view.from.text.toString())
        val to = SimpleDateFormat(Constants.TIME_PATTERN, Locale.US).parse(view.to.text.toString())
        var venue = view.venue.text.toString()
        if (venue == "") venue = "Unknown venue"
        val newSchedule = TimetableInfo(0,name,from,to,venue,day)
        timetableViewModel.insertSchedules(newSchedule)
        dialog.dismiss()
    }

}