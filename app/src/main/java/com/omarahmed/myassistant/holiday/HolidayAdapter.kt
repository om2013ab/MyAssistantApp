package com.omarahmed.myassistant.holiday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.databinding.RvHolidayLayoutBinding

class HolidayAdapter: RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder>() {
    object DiffCallback: DiffUtil.ItemCallback<HolidayInfo>(){
        override fun areItemsTheSame(oldItem: HolidayInfo, newItem: HolidayInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HolidayInfo, newItem: HolidayInfo): Boolean {
           return oldItem == newItem
        }

    }
    val holidayList = AsyncListDiffer(this,DiffCallback)
    class HolidayViewHolder(val binding: RvHolidayLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(holidayInfo: HolidayInfo){
            binding.holidayInfo = holidayInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        return HolidayViewHolder(RvHolidayLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount()= holidayList.currentList.size


    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val currentHoliday = holidayList.currentList[position]
        holder.bind(currentHoliday)
    }
}