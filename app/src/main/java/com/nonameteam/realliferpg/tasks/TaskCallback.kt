package com.nonameteam.realliferpg.tasks

import androidx.recyclerview.widget.DiffUtil
import com.nonameteam.realliferpg.data.TaskData

class TaskCallback (
    private val oldList: List<TaskData>,
    private val newList: List<TaskData>,
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].task_id == newList[newItemPosition].task_id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}