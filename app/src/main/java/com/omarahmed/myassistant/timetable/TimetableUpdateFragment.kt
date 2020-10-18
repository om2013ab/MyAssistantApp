package com.omarahmed.myassistant.timetable

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.databinding.FragmentTimetableUpdateBinding
import com.omarahmed.myassistant.home.HomeViewModel
import com.omarahmed.myassistant.utils.Constants
import com.omarahmed.myassistant.utils.TimePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimetableUpdateFragment : Fragment() {
    private val args: TimetableUpdateFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    private val timetableViewModel: TimetableViewModel by viewModels()
    private lateinit var binding: FragmentTimetableUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimetableUpdateBinding.inflate(inflater)
        binding.from.setOnClickListener {
            TimePicker.timePickerDialog(binding.from, requireContext())
        }
        binding.to.setOnClickListener {
            TimePicker.timePickerDialog(binding.to, requireContext())
        }
        setupDropDownMenu()
        setupToolbar()

        binding.args = args
        return binding.root
    }

    private fun setupDropDownMenu() {
        val nameList = ArrayList<String>()
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            for (name in it) {
                nameList.add(name.courseName)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_layout, nameList)
            binding.name.setAdapter(adapter)
        })

    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarUpdate)
        binding.toolbarUpdate.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_timetable_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                val name = binding.name.text.toString()
                val from = SimpleDateFormat(
                    Constants.TIME_PATTERN,
                    Locale.US
                ).parse(binding.from.text.toString())
                val to = SimpleDateFormat(
                    Constants.TIME_PATTERN,
                    Locale.US
                ).parse(binding.to.text.toString())
                var venue = binding.venue.text.toString()
                if (venue == "") {
                    venue = "Unknown venue"
                }
                val updatedSchedule = TimetableInfo(
                    args.editSchedule.id,
                    name,
                    from,
                    to,
                    venue,
                    args.editSchedule.day
                )
                timetableViewModel.updateSchedule(updatedSchedule)
                findNavController().navigateUp()

            }
        }
        return super.onOptionsItemSelected(item)
    }

}