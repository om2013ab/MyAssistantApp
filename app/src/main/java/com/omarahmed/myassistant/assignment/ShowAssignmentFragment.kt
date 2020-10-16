package com.omarahmed.myassistant.assignment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.cancelAlarm
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.startAlarm
import com.omarahmed.myassistant.data.models.AssignmentInfo
import com.omarahmed.myassistant.databinding.FragmentShowAssignmentBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShowAssignmentFragment : Fragment() {

    private val args: ShowAssignmentFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private lateinit var binding: FragmentShowAssignmentBinding
    private var updateNotificationDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowAssignmentBinding.inflate(inflater)
        setupDropDownMenu()
        setupToolbar()

        binding.btnSave.setOnClickListener {
            updateAssignmentInfo()
        }
        binding.showDeadline.setOnClickListener {
            DatePicker.datePickerDialog(binding.showDeadline, requireContext())
        }

        binding.notificationDateLayout.isVisible = binding.showSwitchAssignment.isChecked

        binding.showSwitchAssignment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.notificationDateLayout.visibility = View.VISIBLE
                binding.notificationDate.setOnClickListener {
                    notificationDatePickerDialog()
                }

            } else {
                binding.notificationDateLayout.visibility = View.GONE
                binding.notificationDate.text!!.clear()
            }
        }
        binding.args = args
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
                    set(Calendar.HOUR, 9)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                updateNotificationDate = c
                binding.notificationDate.setText(
                    SimpleDateFormat(DATE_PATTERN, Locale.US).format(
                        updateNotificationDate!!.time
                    )
                )
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateAssignmentInfo() {
        val name = binding.showName.text.toString()
        val deadline =
            SimpleDateFormat(DATE_PATTERN, Locale.US).parse(binding.showDeadline.text.toString())
        var description = binding.showDescription.text.toString()
        if (description == "") description = "No description"
        val notify = binding.showSwitchAssignment.isChecked
        if (updateNotificationDate != null && updateNotificationDate?.time!!.after(deadline)) {
            Toast.makeText(
                requireContext(),
                "you should choose a notification date that is before the deadline (${binding.showDeadline.text.toString()})",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (binding.notificationDate.text!!.isEmpty() && binding.notificationDateLayout.isVisible) {
                binding.notificationDateLayout.error = "This field is required"
            } else {
                val updatedInfo = AssignmentInfo(
                    args.currentAssignment.id,
                    name,
                    deadline,
                    description,
                    notify,
                    updateNotificationDate?.time
                )
                assignmentViewModel.updateAssignment(updatedInfo)
                cancelAlarm(requireContext(), args.currentAssignment.id)
                updateNotificationDate?.let {
                    startAlarm(
                        requireContext(),
                        args.currentAssignment.id,
                        it.timeInMillis,
                        name,
                        binding.showDeadline.text.toString(),
                        "assignment"
                    )
                }
                findNavController().navigate(R.id.action_showAssignmentFragment_to_assignmentFragment)
            }
        }
    }

    private fun setupDropDownMenu() {
        val nameList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for (name in it) {
                nameList.add(name.courseName)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, nameList)
            binding.showName.setAdapter(adapter)
        })
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                binding.apply {
                    btnSave.visibility = View.VISIBLE
                    codeLayout.isEnabled = true
                    showDeadline.isEnabled = true
                    showDescription.isEnabled = true
                    showSwitchAssignment.isEnabled = true
                    notificationDate.isEnabled = true
                }
            }
            R.id.delete -> {
                assignmentViewModel.deleteAssignment(args.currentAssignment)
                findNavController().navigate(R.id.action_showAssignmentFragment_to_assignmentFragment)
                cancelAlarm(requireContext(), args.currentAssignment.id)
                Snackbar.make(requireView(), "Successfully deleted", Snackbar.LENGTH_LONG).apply {
                    show()
                    setAction("Undo") {
                        assignmentViewModel.insertAssignment(args.currentAssignment)
                        args.currentAssignment.notificationDate?.let {
                            val deadline = SimpleDateFormat(DATE_PATTERN, Locale.US).format(
                                args.currentAssignment.deadLine!!
                            )
                            startAlarm(
                                requireContext(),
                                args.currentAssignment.id,
                                it.time,
                                args.currentAssignment.name,
                                deadline,
                                "assignment"
                            )
                        }

                    }
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

}