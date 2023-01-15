package com.example.mvvmtodo_listapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.databinding.ItemTaskBinding

class TaskRecyclerAdapter(private val onClickListener: TaskOnClickListener) :
    ListAdapter<TaskModel, TaskViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<TaskModel>() {
        override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder.create(parent)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        if (item is TaskModel) {
            holder.bind(item, onClickListener)
        }
    }
}

class TaskViewHolder(private val binding: ItemTaskBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TaskModel, onClickListener: TaskOnClickListener) {
        binding.task = item
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            if(adapterPosition != RecyclerView.NO_POSITION)
                onClickListener.onItemClick(adapterPosition, item)
        }
        binding.checkboxCompleted.setOnClickListener {
            if(adapterPosition != RecyclerView.NO_POSITION)
                onClickListener.onCheckBoxClick(item, binding.checkboxCompleted.isChecked)
        }
    }

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val binding = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return TaskViewHolder(binding)
        }
    }
}

interface TaskOnClickListener {
    fun onItemClick(position: Int, item: TaskModel)
    fun onCheckBoxClick(taskModel: TaskModel , isChecked: Boolean)
}