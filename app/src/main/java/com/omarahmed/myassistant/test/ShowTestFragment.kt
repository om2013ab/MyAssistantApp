package com.omarahmed.myassistant.test

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
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
import com.omarahmed.myassistant.data.models.TestInfo
import com.omarahmed.myassistant.databinding.FragmentShowTestBinding
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.Constants.Companion.TIME_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import com.omarahmed.myassistant.utils.TimePicker
import java.text.SimpleDateFormat
import java.util.*


class ShowTestFragment : Fragment() {
    private val args: ShowTestFragmentArgs by navArgs()
    private val testViewModel: TestViewModel by viewModels()
    private lateinit var binding: FragmentShowTestBinding
    private var updateNotificationDate: Calendar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowTestBinding.inflate(inflater)
        binding.args = args
        setupToolbar()
        binding.btnSave.setOnClickListener {
            updateTestInfo()
        }
        binding.showDate.setOnClickListener {
            DatePicker.datePickerDialog(binding.showDate, requireContext())
        }
        binding.showTime.setOnClickListener {
            TimePicker.timePickerDialog(binding.showTime, requireContext())
        }

        binding.notificationTestDateLayout.isVisible = binding.showSwitchTest.isChecked
        binding.showSwitchTest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.notificationTestDateLayout.visibility = View.VISIBLE
                binding.notificationTestDate.setOnClickListener {
                    notificationDatePickerDialog()
                }
            } else {
                binding.notificationTestDateLayout.visibility = View.GONE
                binding.notificationTestDate.text!!.clear()
            }
        }
        return binding.root
    }

    private fun updateTestInfo() {
        val code = binding.showCode.text.toString()
        val date = SimpleDateFormat(DATE_PATTERN, Locale.US).parse(binding.showDate.text.toString())
        val time = SimpleDateFormat(TIME_PATTERN, Locale.US).parse(binding.showTime.text.toString())
        var chapters = binding.showChapters.text.toString()
        if (chapters == "") chapters = "Not allocated"
        val notify = binding.showSwitchTest.isChecked
        if (updateNotificationDate != null && updateNotificationDate?.time!!.after(date)) {
            Toast.makeText(
                requireContext(),
                "you should choose a notification date that is before the test date (${binding.showDate.text.toString()})",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (binding.notificationTestDate.text!!.isEmpty() && binding.notificationTestDateLayout.isVisible) {
                binding.notificationTestDateLayout.error = "This field is required"
            } else {
                val updatedTest = TestInfo(args.currentTest.id, code, date, time, chapters, notify, updateNotificationDate?.time)
                testViewModel.updateTest(updatedTest)
                cancelAlarm(requireContext(), args.currentTest.id)
                updateNotificationDate?.let {
                    startAlarm(
                        requireContext(),
                        args.currentTest.id,
                        it.timeInMillis,
                        code,
                        binding.showDate.text.toString(),
                        "test"
                    )
                }
                findNavController().navigate(R.id.action_showTestFragment_to_testFragment)
            }
        }

    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
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
                binding.notificationTestDate.setText(
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
                    dateLayout.isEnabled = true
                    timeLayout.isEnabled = true
                    chaptersLayout.isEnabled = true
                    showSwitchTest.isEnabled = true
                }
            }
            R.id.delete -> {
                testViewModel.deleteTest(args.currentTest)
                findNavController().navigate(R.id.action_showTestFragment_to_testFragment)
                Snackbar.make(requireView(), "Successfully deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        testViewModel.insertTest(args.currentTest)
                        args.currentTest.notificationDate?.let {
                            val testDate = SimpleDateFormat(DATE_PATTERN, Locale.US).format(
                                args.currentTest.notificationDate!!
                            )
                            startAlarm(
                                requireContext(),
                                args.currentTest.id,
                                it.time,
                                args.currentTest.code,
                                testDate,
                                "test"
                            )
                        }
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}