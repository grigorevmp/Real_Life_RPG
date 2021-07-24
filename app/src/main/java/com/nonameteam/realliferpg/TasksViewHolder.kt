package com.nonameteam.realliferpg

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nonameteam.realliferpg.data.TaskData

class TasksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var taskName = itemView.findViewById<TextView>(R.id.task_item_name)
    private var taskDescription = itemView.findViewById<TextView>(R.id.task_item_description)

    fun bind(task: TaskData, clickListener: OnTaskItemClickListener) {

        taskName.text = task.task_name

        if(task.task_description != null) {
            if (task.task_description != "") {
                taskDescription.text = task.task_description
            }
            else{
                taskDescription.visibility = View.GONE
            }
        }
        else{
            taskDescription.visibility = View.GONE
        }

        itemView.setOnClickListener {
            clickListener.onTaskItemClick(task)
        }
    }


}