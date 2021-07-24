package com.nonameteam.realliferpg

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nonameteam.realliferpg.data.TaskData
import org.oscim.utils.async.Task

interface OnTaskItemClickListener {
    fun onTaskItemClick(task: TaskData)
}
class TasksAdapter (
    context: Context,
    var tasks: List<TaskData>,
    private val itemClickListener: OnTaskItemClickListener):
    RecyclerView.Adapter<TasksViewHolder>() {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
            return TasksViewHolder(inflater.inflate(R.layout.item_task_card, parent, false))
        }

        override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
            getTaskAt(position)?.let { holder.bind(it, itemClickListener) }
        }

        override fun getItemCount() = tasks.size

        private fun getTaskAt(position: Int): TaskData? {
            return when {
                tasks.isEmpty() -> null
                position >= tasks.size -> null
                else -> tasks[position]
            }
        }
}