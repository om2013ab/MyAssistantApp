package com.omarahmed.myassistant.assignment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.startAlarm
import com.omarahmed.myassistant.data.models.AssignmentInfo
import com.omarahmed.myassistant.databinding.FragmentAddAssignmentBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import com.omarahmed.myassistant.utils.TextWatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddAssignmentFragment : Fragment() {
    private lateinit var binding: FragmentAddAssignmentBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private var notificationDate: Calendar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAssignmentBinding.inflate(inflater)
        setupDropDownMenu()
        binding.deadline.setOnClickListener {
            DatePicker.datePickerDialog(binding.deadline, requireContext())
        }

        binding.btnAdd.setOnClickListener {
            insertAssignments()
        }
        binding.switchAssignment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.notificationDateLayout.visibility = View.VISIBLE
                binding.notificationDate.setOnClickListener {
                    notificationDatePickerDialog()
                }
            } else {
                binding.notificationDateLayout.visibility = View.GONE
                binding.notificationDate.text?.clear()
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        TextWatcher.textChangedAssignment(binding)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun notificationDatePickerDialog() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            requireContext(), { _, year, month, day ->
                c.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY,9)
                    set(Calendar.MINUTE,0)
                    set(Calendar.SECOND,0)
                    set(Calendar.MILLISECOND,0)
                }
                notificationDate = c
                binding.notificationDate.setText(
                    SimpleDateFormat(DATE_PATTERN, Locale.US).format(
                        notificationDate!!.time
                    )
                )
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupDropDownMenu() {
        val codeList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for (code in it) {
                codeList.add(code.courseCode)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, codeList)
            binding.codeAssignment.setAdapter(adapter)
        })

    }

    private fun insertAssignments() {
        val id = Random().nextInt()
        val code = binding.codeAssignment.text.toString()
        val deadline =
            SimpleDateFormat(DATE_PATTERN, Locale.US).parse(binding.deadline.text.toString())
        var description = binding.description.text.toString()
        if (description == "") description = "No description"
        val notify = binding.switchAssignment.isChecked
        if (notificationDate != null && notificationDate?.time!!.after(deadline)) {
            Toast.makeText(
                requireContext(),
                "you should choose a notification date that is before the deadline (${binding.deadline.text.toString()})",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (binding.notificationDate.text!!.isEmpty() && binding.notificationDateLayout.isVisible) {
                binding.notificationDateLayout.error = "This field is required"
            } else {
                val newAssignment = AssignmentInfo(id, code, deadline, description, notify,notificationDate?.time)
                assignmentViewModel.insertAssignment(newAssignment)
                findNavController().navigate(R.id.action_addAssignmentFragment2_to_assignmentFragment)
                notificationDate?.let {
                    startAlarm(requireContext(),id,it.timeInMillis,code,binding.deadline.text.toString(),"assignment")
                }
            }
        }

    }
}
