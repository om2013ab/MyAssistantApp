package com.omarahmed.myassistant.test

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowTestBinding.inflate(inflater)
        binding.args = args
        setupToolbar()
        binding.btnSave.setOnClickListener {
            updateTestInfo()
            findNavController().navigateUp()
        }
        binding.showDate.setOnClickListener {
            DatePicker.datePickerDialog(binding.showDate,requireContext())
        }
        binding.showTime.setOnClickListener {
            TimePicker.timePickerDialog(binding.showTime,requireContext())
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
        val updatedTest = TestInfo(args.currentTest.id,code,date,time,chapters,notify)
        testViewModel.updateTest(updatedTest)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
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
            R.id.edit ->{
                binding.apply {
                    btnSave.visibility = View.VISIBLE
                    codeLayout.isEnabled = true
                    dateLayout.isEnabled = true
                    timeLayout.isEnabled = true
                    chaptersLayout.isEnabled = true
                    showSwitchTest.isEnabled = true
                }
            }
            R.id.delete ->{
                testViewModel.deleteTest(args.currentTest)
                findNavController().navigateUp()
                Snackbar.make(requireView(),"Successfully deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO"){
                        testViewModel.insertTest(args.currentTest)
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}