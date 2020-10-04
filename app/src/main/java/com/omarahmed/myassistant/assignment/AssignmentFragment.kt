package com.omarahmed.myassistant.assignment

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
import com.omarahmed.myassistant.data.models.AssignmentInfo
import com.omarahmed.myassistant.databinding.DialogAddAssignmentBinding
import com.omarahmed.myassistant.databinding.FragmentAssignmentBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants.Companion.DATE_PATTERN
import com.omarahmed.myassistant.utils.DatePicker
import com.omarahmed.myassistant.utils.TextWatcher
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AssignmentFragment : Fragment() {
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = FragmentAssignmentBinding.inflate(inflater)
        val assignmentAdapter = AssignmentAdapter()
        binding.rvAssignments.adapter = assignmentAdapter

        assignmentViewModel.getAllAssignment.observe(viewLifecycleOwner, {
            assignmentViewModel.checkDatabaseEmpty(it)
            assignmentAdapter.assignmentList.submitList(it)
        })
        binding.floatingActionButton.setOnClickListener {
            setupAddAssignmentDialog()
        }
        binding.lifecycleOwner = this
        binding.assignmentViewModel = assignmentViewModel
        //setToolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAssignment)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupAddAssignmentDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = DialogAddAssignmentBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(view.root)
        val dialog = builder.create()
        dialog.apply {
            show()
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setupDropDownMenu(view)
        view.deadline.setOnClickListener {
            DatePicker.datePickerDialog(view.deadline, requireContext())
        }

        view.btnAdd.setOnClickListener {
            insertAssignments(view, dialog)
        }

        view.btnCancel.setOnClickListener {
            view.switchAssignment.isChecked = false
            dialog.dismiss()
        }
        TextWatcher.textChangedAssignment(view)
    }

    private fun insertAssignments(view: DialogAddAssignmentBinding, dialog: AlertDialog) {
        val code = view.codeAssignment.text.toString()
        val deadline = SimpleDateFormat(DATE_PATTERN, Locale.US).parse(view.deadline.text.toString())
        var description = view.description.text.toString()
        if (description == "") description = "No description"
        val notify = view.switchAssignment.isChecked
        val newAssignment = AssignmentInfo(0, code, deadline, description, notify)
        assignmentViewModel.insertAssignment(newAssignment)
        dialog.dismiss()
    }


    private fun setupDropDownMenu(view: DialogAddAssignmentBinding) {
        val codeList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, Observer {
            for (code in it) {
                codeList.add(code.courseCode)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, codeList)
            view.codeAssignment.setAdapter(adapter)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll && assignmentViewModel.noAssignment.value == false) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete all")
                .setMessage("Are you sure you want to delete all current assignments")
                .setPositiveButton("YES") { _, _ ->
                    assignmentViewModel.deleteAllAssignments()
                }
                .setNegativeButton("NO") { _, _ ->
                }
                .show()
        } else {
            Toast.makeText(
                requireContext(),
                "You do not have any assignment to delete",
                Toast.LENGTH_SHORT
            ).show()
        }
        return false
    }

}
