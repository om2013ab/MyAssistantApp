package com.omarahmed.myassistant.timetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.databinding.RvTimetableLayoutBinding

class TimetableAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    object DiffCallback: DiffUtil.ItemCallback<TimetableInfo>(){
        override fun areItemsTheSame(oldItem: TimetableInfo, newItem: TimetableInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimetableInfo, newItem: TimetableInfo): Boolean {
            return oldItem == newItem
        }

    }

    val scheduleList = AsyncListDiffer(this,DiffCallback)

    class TimetableViewHolder(val binding: RvTimetableLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(timetableInfo: TimetableInfo){
            binding.timetableInfo = timetableInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TimetableViewHolder(RvTimetableLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount() = scheduleList.currentList.size

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val currentSchedule = scheduleList.currentList[position]
        holder.bind(currentSchedule)

        holder.binding.moreOption.setOnClickListener {
            onClickListener.onClick(currentSchedule,holder.binding.moreOption)
        }
    }

    class OnClickListener(val clickListener:(timetableInfo: TimetableInfo, view:View)-> Unit){
        fun onClick(timetableInfo: TimetableInfo, view: View) = clickListener(timetableInfo,view)
    }


}