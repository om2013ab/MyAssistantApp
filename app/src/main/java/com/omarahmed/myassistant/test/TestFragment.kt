package com.omarahmed.myassistant.test

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.data.models.TestInfo
import com.omarahmed.myassistant.databinding.DialogAddTestBinding
import com.omarahmed.myassistant.databinding.FragmentTestBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.Constants.Companion.TIME_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import com.omarahmed.myassistant.utils.TextWatcher
import com.omarahmed.myassistant.utils.TimePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val testViewModel: TestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestBinding.inflate(inflater)
        val testAdapter = TestAdapter()
        binding.rvTests.adapter = testAdapter

        binding.floatingActionButton.setOnClickListener {
            setupAddTestDialog()
        }
        testViewModel.getAllTests.observe(viewLifecycleOwner,{
            testViewModel.checkTestEmpty(it)
            testAdapter.testList.submitList(it)
        })
        //setup toolbar
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarTest)

        binding.lifecycleOwner = this
        binding.testViewModel = testViewModel
        return binding.root
    }

    private fun setupAddTestDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = DialogAddTestBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(view.root)
        val dialog = builder.create()
        dialog.apply {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        setupDropDownMenu(view)
        view.btnCancel.setOnClickListener { dialog.dismiss() }
        view.dateTest.setOnClickListener {
            DatePicker.datePickerDialog(view.dateTest, requireContext())
        }
        view.timeTest.setOnClickListener {
            TimePicker.timePickerDialog(view.timeTest, requireContext())
        }
        view.btnAdd.setOnClickListener {
            insertTests(view, dialog)
        }
        TextWatcher.textChangedTest(view)

    }

    private fun insertTests(view: DialogAddTestBinding, dialog: AlertDialog) {
        val code = view.codeTest.text.toString()
        val date = SimpleDateFormat(DATE_PATTERN, Locale.US).parse(view.dateTest.text.toString())
        val time = SimpleDateFormat(TIME_PATTERN, Locale.US).parse(view.timeTest.text.toString())
        var chapters = view.chapters.text.toString()
        if (chapters == "") chapters = "Not allocated"
        val notify = view.switchTest.isChecked
        val newTest = TestInfo(0, code, date, time, chapters, notify)
        testViewModel.insertTest(newTest)
        dialog.dismiss()
    }

    private fun setupDropDownMenu(view: DialogAddTestBinding) {
        val codeList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, Observer {
            for (code in it) {
                codeList.add(code.courseCode)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, codeList)
            view.codeTest.setAdapter(adapter)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll && testViewModel.noTest.value == false) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete all")
                .setMessage("Are you sure you want to delete all current tests")
                .setPositiveButton("YES") { _, _ ->
                    testViewModel.deleteAllTests()
                }
                .setNegativeButton("NO") { _, _ -> }
                .show()

        } else {
            Toast.makeText(
                requireContext(),
                "You do not have any test to delete",
                Toast.LENGTH_SHORT
            ).show()

        }
        return false
    }
}