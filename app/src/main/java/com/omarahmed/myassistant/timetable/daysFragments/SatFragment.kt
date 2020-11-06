package com.omarahmed.myassistant.timetable.daysFragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.databinding.DialogAddSchedualBinding
import com.omarahmed.myassistant.databinding.FragmentSatBinding
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


class SatFragment : Fragment() {
    private val timetableViewModel: TimetableViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSatBinding.inflate(inflater)
        val timetableAdapter =
            TimetableAdapter(TimetableAdapter.OnClickListener { currentSchedule, view ->
                setupPopupMenu(currentSchedule, view)
            })
        binding.fabSaturday.setOnClickListener {
            setupAddSchedule()
        }
        binding.rvSaturday.adapter = timetableAdapter
        timetableViewModel.getSchedule("Saturday").observe(viewLifecycleOwner,  {
            timetableAdapter.scheduleList.submitList(it)
        })
        return binding.root
    }

    private fun setupPopupMenu(currentSchedule: TimetableInfo, view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popupMenu.apply {
            menuInflater.inflate(R.menu.options_menu, popupMenu.menu)
            show()
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        val action =
                            TimetableFragmentDirections.actionTimetableFragmentToTimetableUpdateFragment(
                                currentSchedule
                            )
                        findNavController().navigate(action)
                    }
                    R.id.delete -> {
                        timetableViewModel.deleteSchedule(currentSchedule)
                        Snackbar.make(requireView(), "Successfully deleted", Snackbar.LENGTH_LONG)
                            .apply {
                                setAction("UNDO") {
                                    timetableViewModel.insertSchedules(currentSchedule)
                                }
                                show()
                            }
                    }
                }
                false
            }
        }
    }

    private fun setupAddSchedule() {
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
        view.btnAdd.setOnClickListener { insertNewSchedule(view, dialog) }
        view.from.setOnClickListener { TimePicker.timePickerDialog(view.from, requireContext()) }
        view.to.setOnClickListener { TimePicker.timePickerDialog(view.to, requireContext()) }
        TextWatcher.textChangedTimetable(view)

    }

    private fun setupDropDownMenu(view: DialogAddSchedualBinding) {
        val nameList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for (name in it) {
                nameList.add(name.courseName)
            }
        })
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, nameList)
        view.name.setAdapter(adapter)

    }

    private fun insertNewSchedule(view: DialogAddSchedualBinding, dialog: AlertDialog) {
        val name = view.name.text.toString()
        val from = SimpleDateFormat(Constants.TIME_PATTERN, Locale.US).parse(view.from.text.toString())
        val to = SimpleDateFormat(Constants.TIME_PATTERN, Locale.US).parse(view.to.text.toString())
        var venue = view.venue.text?.trim().toString()
        if (venue == "") venue = "Unknown venue"
        val newSchedule = TimetableInfo(0, name, from, to, venue,"Saturday")
        timetableViewModel.insertSchedules(newSchedule)
        dialog.dismiss()
    }



}



