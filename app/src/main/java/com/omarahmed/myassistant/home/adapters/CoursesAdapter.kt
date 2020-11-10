package com.omarahmed.myassistant.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.databinding.RvCoursesLayoutBinding
import com.omarahmed.myassistant.home.CourseInfo

class CoursesAdapter(
    private val listener: OnCourseClickListener
) : RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
    object DiffCallback : DiffUtil.ItemCallback<CourseInfo>() {
        override fun areItemsTheSame(oldItem: CourseInfo, newItem: CourseInfo): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: CourseInfo, newItem: CourseInfo): Boolean {
            return oldItem == newItem
        }

    }
    val coursesList = AsyncListDiffer(
        this,
        DiffCallback
    )

    inner class CourseViewHolder(private val binding: RvCoursesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,View.OnLongClickListener{
        fun bind(courseInfo: CourseInfo) {
            binding.courseInfo = courseInfo
            binding.executePendingBindings()
        }
        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onCourseClick(coursesList.currentList[position])
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onLongCourseClick(coursesList.currentList[position])
            }
            return true
        }
    }

    override fun getItemCount() = coursesList.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            RvCoursesLayoutBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentCourse = coursesList.currentList[position]
        holder.bind(currentCourse)
    }

    interface OnCourseClickListener {
        fun onCourseClick(courseInfo: CourseInfo)
        fun onLongCourseClick(courseInfo: CourseInfo)
    }
}