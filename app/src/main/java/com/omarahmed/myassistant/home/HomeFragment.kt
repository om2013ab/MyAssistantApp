package com.omarahmed.myassistant.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.alarmmanager.ScheduleAlarm.Companion.cancelAlarm
import com.omarahmed.myassistant.assignment.AssignmentViewModel
import com.omarahmed.myassistant.databinding.DialogAddCourseBinding
import com.omarahmed.myassistant.databinding.FragmentHomeBinding
import com.omarahmed.myassistant.databinding.SheetCourseInfoBinding
import com.omarahmed.myassistant.home.adapters.AssignmentHomeAdapter
import com.omarahmed.myassistant.home.adapters.CoursesAdapter
import com.omarahmed.myassistant.home.adapters.TestHomeAdapter
import com.omarahmed.myassistant.test.TestViewModel
import com.omarahmed.myassistant.timetable.TimetableViewModel
import com.omarahmed.myassistant.utils.TextWatcher
import kotlinx.android.synthetic.main.dialog_long_press_course.view.*

class HomeFragment : Fragment(),CoursesAdapter.OnCourseClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private val testViewModel: TestViewModel by viewModels()
    private val timetableViewModel: TimetableViewModel by viewModels()
    private lateinit var assignmentAdapter: AssignmentHomeAdapter
    private lateinit var testAdapter: TestHomeAdapter
    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        assignmentAdapter = AssignmentHomeAdapter()
        testAdapter = TestHomeAdapter()
        coursesAdapter = CoursesAdapter(this)

        timetableViewModel.getScheduleToCheckEmpty.observe(viewLifecycleOwner, {
            timetableViewModel.checkEmptyTimetable(it)
        })

        binding.addCourse.setOnClickListener {
            showAddDialog()
        }

        setupToolbar()
        setCoursesRecyclerView(coursesAdapter)
        setAssignmentsRecyclerView(assignmentAdapter)
        setTestRecyclerView(testAdapter)
        binding.lifecycleOwner = this
        binding.homeViewModel = homeViewModel
        binding.assignmentViewModel = assignmentViewModel
        binding.testViewModel = testViewModel
        return binding.root
    }

    override fun onCourseClick(courseInfo: CourseInfo) {
        setupBottomSheet(courseInfo)
    }

    override fun onLongCourseClick(courseInfo: CourseInfo) {
        setupLongPressDialog(courseInfo)
    }

    private fun setupLongPressDialog(courseInfo: CourseInfo) {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_long_press_course, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
        view.add_assignment.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToAddAssignmentFragment(courseInfo)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        view.add_test.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddTestFragment(courseInfo)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        view.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }


    private fun setCoursesRecyclerView(coursesAdapter: CoursesAdapter) {
        binding.rvCourses.adapter = coursesAdapter
        homeViewModel.getAllCourses.observe(viewLifecycleOwner, {
            homeViewModel.checkCoursesEmpty(it)
            coursesAdapter.coursesList.submitList(it)
        })
    }

    private fun setAssignmentsRecyclerView(assignmentAdapter: AssignmentHomeAdapter) {
        binding.rvAssignments.adapter = assignmentAdapter
        assignmentViewModel.getAllAssignment.observe(viewLifecycleOwner, {
            assignmentViewModel.checkDatabaseEmpty(it)
            assignmentAdapter.assignmentList.submitList(it)
        })
    }

    private fun setTestRecyclerView(testAdapter: TestHomeAdapter) {
        binding.rvTests.adapter = testAdapter
        testViewModel.getAllTests.observe(viewLifecycleOwner, {
            testViewModel.checkTestEmpty(it)
            testAdapter.testList.submitList(it)
        })
    }


    private fun showAddDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view = DialogAddCourseBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(view.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        view.btnAddCourse.setOnClickListener {
            insertCourse(view, dialog)
        }
        view.btnDismissAddCourse.setOnClickListener {
            dialog.dismiss()
        }
        TextWatcher.textChangedHome(view)
    }

    private fun insertCourse(view: DialogAddCourseBinding, dialog: AlertDialog) {
        val courseName = view.courseName.text.toString()
        val courseCode = view.courseCode.text.toString()
        val creditHours = view.creditHours.text.toString()
        val lecturerName = view.lecturerName.text.toString()
        val courseInfo = CourseInfo(
            0,
            courseName,
            courseCode,
            creditHours,
            lecturerName
        )
        homeViewModel.insertCourse(courseInfo)
        dialog.dismiss()
    }

    private fun setupBottomSheet(currentCourse: CourseInfo) {
        val bottomSheet = BottomSheetDialog(requireActivity(), R.style.bottomSheet)
        val view = SheetCourseInfoBinding.inflate(LayoutInflater.from(activity))
        bottomSheet.apply {
            setContentView(view.root)

            show()
        }
        view.aboutCourse = currentCourse
        view.btnMore.setOnClickListener {
            setupPopMenu(view.btnMore, currentCourse, bottomSheet, view)
        }

        view.btnSave.setOnClickListener {
            updateCourseInfo(view, bottomSheet, currentCourse)
        }

    }

    private fun updateCourseInfo(
        view: SheetCourseInfoBinding,
        bottomSheet: BottomSheetDialog,
        currentCourse: CourseInfo
    ) {
        val updatedName = view.showName.text.toString()
        val updatedCode = view.showCode.text.toString()
        val updatedCredit = view.showCredit.text.toString()
        val updatedLecturer = view.showLecturer.text.toString()
        val updatedInfo = CourseInfo(
            currentCourse.courseId,
            updatedName,
            updatedCode,
            updatedCredit,
            updatedLecturer
        )
        homeViewModel.updateCourse(updatedInfo)
        bottomSheet.dismiss()
    }

    private fun setupPopMenu(
        view: ImageView,
        currentCourse: CourseInfo,
        bottomSheetDialog: BottomSheetDialog,
        bottomSheetBinding: SheetCourseInfoBinding
    ) {
        val popMenu = PopupMenu(requireContext(), view)
        popMenu.apply {
            menuInflater.inflate(R.menu.options_menu, popMenu.menu)
            show()
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> handleFocusability(bottomSheetBinding)
                    R.id.delete -> {
                        homeViewModel.deleteCourse(currentCourse)
                        bottomSheetDialog.dismiss()
                        Snackbar.make(requireView(), "Successfully deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                homeViewModel.insertCourse(currentCourse)
                            }
                            .show()
                    }
                }
                false
            }
        }
    }

    private fun handleFocusability(bottomSheetBinding: SheetCourseInfoBinding) {
        bottomSheetBinding.apply {
            btnSave.visibility = View.VISIBLE
            showName.isFocusableInTouchMode = true
            showCode.isFocusableInTouchMode = true
            showCredit.isFocusableInTouchMode = true
            showLecturer.isFocusableInTouchMode = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll
            && homeViewModel.noCourses.value == false
            || assignmentViewModel.noAssignment.value == false
            || testViewModel.noTest.value == false
            || timetableViewModel.emptyTimetable.value == false
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Are you sure ?")
                .setMessage("This will delete all current courses, schedules, assignments, and tests")
                .setPositiveButton("YES") { _, _ ->
                    homeViewModel.deleteAllCourses()
                    assignmentViewModel.deleteAllAssignments()
                    assignmentAdapter.assignmentList.currentList.forEach {
                        cancelAlarm(requireContext(), it.id)
                    }
                    testViewModel.deleteAllTests()
                    testAdapter.testList.currentList.forEach {
                        cancelAlarm(requireContext(), it.id)
                    }
                    timetableViewModel.deleteAllSchedules()
                    Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("NO") { _, _ -> }
                .show()
        } else {
            Toast.makeText(requireContext(), "Nothing to delete", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}