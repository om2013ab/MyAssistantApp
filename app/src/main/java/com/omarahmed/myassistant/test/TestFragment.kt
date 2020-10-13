package com.omarahmed.myassistant.test

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
import com.omarahmed.myassistant.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    private val testViewModel: TestViewModel by viewModels()
    private lateinit var testAdapter: TestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTestBinding.inflate(inflater)
        testAdapter = TestAdapter()
        binding.rvTests.adapter = testAdapter

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_testFragment_to_addTestFragment)
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
                    testAdapter.testList.currentList.forEach {
                        cancelAlarm(requireContext(),it.id)
                    }
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