package com.omarahmed.myassistant.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.databinding.RvCoursesLayoutBinding
import com.omarahmed.myassistant.home.CourseInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_HEADER_TYPE = 0
private const val ITEM_COURSE_TYPE = 1

class CoursesAdapter(
    private val listener: OnCourseClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSubmitList(list: List<CourseInfo>?) {
        adapterScope.launch {
            val item = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {
                    DataItem.CourseInfoItem(it)
                }
            }
            withContext(Dispatchers.Main) {
                submitList(item)
            }
        }

    }

    inner class CourseViewHolder(private val binding: RvCoursesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        fun bind( dataItem: DataItem.CourseInfoItem) {
            binding.courseInfo = dataItem.courseInfo
            binding.executePendingBindings()
        }

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val currentCourse = getItem(adapterPosition) as DataItem.CourseInfoItem
                listener.onCourseClick(currentCourse.courseInfo)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val currentCourse = getItem(adapterPosition) as DataItem.CourseInfoItem
                listener.onLongCourseClick(currentCourse.courseInfo)
            }
            return true
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onAddCourseClick()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_HEADER_TYPE
            is DataItem.CourseInfoItem -> ITEM_COURSE_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER_TYPE -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.add_course_layout, parent, false)
            )
            ITEM_COURSE_TYPE -> CourseViewHolder(
                RvCoursesLayoutBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CourseViewHolder -> {
                val currentCourse = getItem(position) as DataItem.CourseInfoItem
                holder.bind(currentCourse)
            }
        }

    }

    interface OnCourseClickListener {
        fun onCourseClick(courseInfo: CourseInfo)
        fun onLongCourseClick(courseInfo: CourseInfo)
        fun onAddCourseClick()
    }
}

sealed class DataItem {
    abstract val id: Int

    data class CourseInfoItem(val courseInfo: CourseInfo) : DataItem() {
        override val id = courseInfo.courseId
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }
}