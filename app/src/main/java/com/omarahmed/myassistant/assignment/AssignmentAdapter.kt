package com.omarahmed.myassistant.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.databinding.RvAssignmentsLayoutBinding

class AssignmentAdapter : RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {
    object DiffCallback : DiffUtil.ItemCallback<AssignmentInfo>() {
        override fun areItemsTheSame(oldItem: AssignmentInfo, newItem: AssignmentInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AssignmentInfo, newItem: AssignmentInfo): Boolean {
            return oldItem == newItem
        }

    }

    val assignmentList = AsyncListDiffer(this, DiffCallback)

    class AssignmentViewHolder(private val binding: RvAssignmentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(assignmentInfo: AssignmentInfo) {
            binding.assignmentInfo = assignmentInfo
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AssignmentViewHolder(
        RvAssignmentsLayoutBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun getItemCount() = assignmentList.currentList.size

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val currentAssignment = assignmentList.currentList[position]
        holder.bind(currentAssignment)
        holder.itemView.setOnClickListener {
            val action =
                AssignmentFragmentDirections.actionAssignmentFragmentToShowAssignmentFragment(
                    currentAssignment
                )
            it.findNavController().navigate(action)
        }
    }
}