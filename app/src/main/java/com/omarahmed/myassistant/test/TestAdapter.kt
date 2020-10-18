package com.omarahmed.myassistant.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.databinding.RvTestsLayoutBinding

class TestAdapter:RecyclerView.Adapter<TestAdapter.TestViewHolder>() {
    object DiffCallback: DiffUtil.ItemCallback<TestInfo>(){
        override fun areItemsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TestInfo, newItem: TestInfo): Boolean {
            return oldItem == newItem
        }

    }
    val testList = AsyncListDiffer(this,DiffCallback)

    class TestViewHolder(val binding: RvTestsLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(testInfo: TestInfo){
            binding.testInfo = testInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TestViewHolder(RvTestsLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount() = testList.currentList.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val currentTest = testList.currentList[position]
        holder.bind(currentTest)
        holder.itemView.setOnClickListener {
            val action = TestFragmentDirections.actionTestFragmentToShowTestFragment(currentTest)
            it.findNavController().navigate(action)
        }

    }
}