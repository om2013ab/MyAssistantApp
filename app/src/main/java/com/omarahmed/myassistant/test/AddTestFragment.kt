package com.omarahmed.myassistant.test

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
import androidx.navigation.fragment.navArgs
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.startAlarm
import com.omarahmed.myassistant.databinding.FragmentAddTestBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import com.omarahmed.myassistant.utils.TextWatcher
import com.omarahmed.myassistant.utils.TimePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTestFragment : Fragment() {
    private lateinit var binding: FragmentAddTestBinding
    private val args: AddTestFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    private val testViewModel: TestViewModel by viewModels()
    private var notificationDate: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTestBinding.inflate(inflater)
        setupDropDownMenu()
        binding.btnCancel.setOnClickListener { findNavController().navigateUp()}
        binding.testDate.setOnClickListener {
            DatePicker.datePickerDialog(binding.testDate, requireContext())
        }
        binding.testTime.setOnClickListener {
            TimePicker.timePickerDialog(binding.testTime, requireContext())
        }
        binding.btnAdd.setOnClickListener {
            insertTests()
        }
        binding.switchTest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.notificationTestDateLayout.visibility = View.VISIBLE
                binding.notificationTestDate.setOnClickListener {
                    notificationDatePickerDialog()
                }
            } else {
                binding.notificationTestDateLayout.visibility = View.GONE
                binding.notificationTestDate.text?.clear()
            }
        }
        TextWatcher.textChangedTest(binding)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.testArgs = args
        return binding.root
    }

    private fun setupDropDownMenu() {
        val nameList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner,{
            for (name in it) {
                nameList.add(name.courseName)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, nameList)
            binding.testName.setAdapter(adapter)
        })

    }

    private fun notificationDatePickerDialog(){
        val c = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),{ _, year, month, day ->
                c.apply {
                    set(Calendar.YEAR,year)
                    set(Calendar.MONTH,month)
                    set(Calendar.DAY_OF_MONTH,day)
                    set(Calendar.HOUR,9)
                    set(Calendar.MINUTE,0)
                    set(Calendar.SECOND,0)
                    set(Calendar.MILLISECOND,0)
                }
                notificationDate = c
                binding.notificationTestDate.setText(
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

    private fun insertTests() {
        val id = Random().nextInt()
        val name = binding.testName.text.toString()
        val date = SimpleDateFormat(DATE_PATTERN, Locale.US).parse(binding.testDate.text.toString())
        val time = SimpleDateFormat(Constants.TIME_PATTERN, Locale.US).parse(binding.testTime.text.toString())
        var chapters = binding.chapters.text.toString()
        if (chapters == "") chapters = "Not allocated"
        val notify = binding.switchTest.isChecked
        if (notificationDate != null && notificationDate?.time!!.after(date)){
            Toast.makeText(
                requireContext(),
                "you should choose a notification date that is before the test date (${binding.testDate.text.toString()})",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (binding.notificationTestDate.text!!.isEmpty() && binding.notificationTestDateLayout.isVisible){
                binding.notificationTestDateLayout.error = "This field is required"
            } else {
                val newTest = TestInfo(id, name, date, time, chapters, notify,notificationDate?.time)
                testViewModel.insertTest(newTest)
                findNavController().navigate(R.id.action_addTestFragment_to_testFragment)
                notificationDate?.let {
                    startAlarm(requireContext(),id,it.timeInMillis,name,binding.testDate.text.toString(),"test")
                }
            }
        }

    }

}