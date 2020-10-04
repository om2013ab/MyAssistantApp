package com.omarahmed.myassistant.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.R
import com.omarahmed.myassistant.data.models.AssignmentInfo
import com.omarahmed.myassistant.databinding.RvAssignmentsHomeBinding

class AssignmentHomeAdapter: RecyclerView.Adapter<AssignmentHomeAdapter.AssignmentViewHolder>() {
    object DiffCallback : DiffUtil.ItemCallback<AssignmentInfo>() {
        override fun areItemsTheSame(oldItem: AssignmentInfo, newItem: AssignmentInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AssignmentInfo, newItem: AssignmentInfo): Boolean {
            return oldItem == newItem
        }

    }

    val assignmentList = AsyncListDiffer(this, DiffCallback)

    class AssignmentViewHolder(private val binding: RvAssignmentsHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(assignmentInfo: AssignmentInfo) {
            binding.assignmentInfo = assignmentInfo
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AssignmentViewHolder(
        RvAssignmentsHomeBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun getItemCount() = assignmentList.currentList.size

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val currentAssignment = assignmentList.currentList[position]
        holder.bind(currentAssignment)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_assignmentFragment)
        }
    }
}