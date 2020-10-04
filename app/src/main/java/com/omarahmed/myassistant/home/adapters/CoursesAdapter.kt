package com.omarahmed.myassistant.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.data.models.CourseInfo
import com.omarahmed.myassistant.databinding.RvCoursesLayoutBinding

class CoursesAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
    object DiffCallback:DiffUtil.ItemCallback<CourseInfo>() {
        override fun areItemsTheSame(oldItem: CourseInfo, newItem: CourseInfo): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: CourseInfo, newItem: CourseInfo): Boolean {
           return oldItem == newItem
        }

    }
    val coursesList = AsyncListDiffer(this,
        DiffCallback
    )
    class CourseViewHolder(private val binding: RvCoursesLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(courseInfo: CourseInfo){
            binding.courseInfo = courseInfo
            binding.executePendingBindings()
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
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentCourse)
        }
    }
    class OnClickListener(val clickListener:(courseInfo: CourseInfo)-> Unit){
        fun onClick(courseInfo: CourseInfo) = clickListener(courseInfo)
    }

}