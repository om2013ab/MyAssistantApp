package com.omarahmed.myassistant.assignment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.cancelAlarm
import com.omarahmed.myassistant.databinding.FragmentAssignmentBinding


class AssignmentFragment : Fragment() {
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private lateinit var assignmentAdapter: AssignmentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val binding = FragmentAssignmentBinding.inflate(inflater)
        assignmentAdapter = AssignmentAdapter()
        binding.rvAssignments.adapter = assignmentAdapter

        assignmentViewModel.getAllAssignment.observe(viewLifecycleOwner, {
            assignmentViewModel.checkDatabaseEmpty(it)
            assignmentAdapter.assignmentList.submitList(it)
        })
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_assignmentFragment_to_addAssignmentFragment)
        }
        binding.lifecycleOwner = this
        binding.assignmentViewModel = assignmentViewModel
        //setToolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAssignment)
        setHasOptionsMenu(true)
        return binding.root
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
                    assignmentAdapter.assignmentList.currentList.forEach {
                        cancelAlarm(requireContext(),it.id)
                    }
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
