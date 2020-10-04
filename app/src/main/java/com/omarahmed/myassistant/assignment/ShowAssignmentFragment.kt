package com.omarahmed.myassistant.assignment

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
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
    private val homeViewModel:HomeViewModel by viewModels()
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private lateinit var binding:FragmentShowAssignmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowAssignmentBinding.inflate(inflater)
        setupToolbar()

        setupDropDownMenu()
        binding.btnSave.setOnClickListener {
            updateAssignmentInfo()
            findNavController().navigateUp()
        }
        binding.showDeadline.setOnClickListener {
            DatePicker.datePickerDialog(binding.showDeadline,requireContext())
        }
        binding.args = args
        return binding.root

    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit->{
                binding.apply {
                    btnSave.visibility = View.VISIBLE
                    codeLayout.isEnabled = true
                    showDeadline.isEnabled = true
                    showDescription.isEnabled = true
                    showSwitchAssignment.isEnabled = true
                }
            }
            R.id.delete -> {
                assignmentViewModel.deleteAssignment(args.currentAssignment)
                findNavController().navigateUp()
                Snackbar.make(requireView(),"Successfully deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        assignmentViewModel.insertAssignment(args.currentAssignment)
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateAssignmentInfo() {
        val code = binding.showCode.text.toString()
        val deadline = SimpleDateFormat(DATE_PATTERN, Locale.US).parse(binding.showDeadline.text.toString())
        var description = binding.showDescription.text.toString()
        if (description == "") description = "No description"
        val notify = binding.showSwitchAssignment.isChecked
        val updatedInfo = AssignmentInfo(args.currentAssignment.id,code,deadline,description,notify)
        assignmentViewModel.updateAssignment(updatedInfo)
    }

    private fun setupDropDownMenu() {
        val codeList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for(code in it){
                codeList.add(code.courseCode)
            }
            val adapter = ArrayAdapter(requireContext(),R.layout.drop_down_layout,codeList)
            binding.showCode.setAdapter(adapter)
        })

    }

}