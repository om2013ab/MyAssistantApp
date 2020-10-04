package com.omarahmed.myassistant.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.data.models.TestInfo
import com.omarahmed.myassistant.databinding.RvTestHomeBinding

class TestHomeAdapter:RecyclerView.Adapter<TestHomeAdapter.TestViewHolder>() {
    object DiffCallback: DiffUtil.ItemCallback<TestInfo>(){
        override fun areItemsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
            return oldItem == newItem
        }

    }
    val testList = AsyncListDiffer(this,DiffCallback)

    class TestViewHolder(val binding: RvTestHomeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(testInfo: TestInfo){
            binding.testInfo = testInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TestViewHolder(RvTestHomeBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount() = testList.currentList.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val currentTest = testList.currentList[position]
        holder.bind(currentTest)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_testFragment)
        }

    }
}