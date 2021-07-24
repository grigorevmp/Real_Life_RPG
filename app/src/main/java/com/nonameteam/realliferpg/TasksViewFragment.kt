package com.nonameteam.realliferpg

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nonameteam.realliferpg.data.TaskData
import com.nonameteam.realliferpg.helpers.TaskDbHelper

class TasksViewFragment: Fragment() {
    private lateinit  var taskName: TextView
    private lateinit var tasksDescription: TextView
    private lateinit var dbHelper: TaskDbHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_view, container, false)

        dbHelper = TaskDbHelper(view.context)

        val taskID = arguments?.getInt("task_id")

        val task = getDataInfo(taskID)

        taskName = view.findViewById(R.id.task_name)
        tasksDescription = view.findViewById(R.id.task_description)

        taskName.text = task!!.task_name
        if(task.task_description != null) {
            if (task.task_description != "") {
                tasksDescription.text = task.task_description
            }
            else{
                tasksDescription.visibility = View.GONE
            }
        }
        else{
            tasksDescription.visibility = View.GONE
        }

        val deleteTaskButton = view.findViewById<FloatingActionButton>(R.id.delete_task)

        deleteTaskButton.setOnClickListener { deleteTask(taskID)}

        return view
    }

    private fun deleteTask(taskID: Int?) {

        val database: SQLiteDatabase = dbHelper.writableDatabase

        val selection = TaskDbHelper.TASK_ID + "=?";
        val selectionArgs = arrayOf(taskID.toString())

        database.delete(TaskDbHelper.TABLE_NAME, selection , selectionArgs)

        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit();
    }

    private fun getDataInfo(taskID: Int?): TaskData? {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        var taskData: TaskData?

        val selection = TaskDbHelper.TASK_ID + "=?";
        val selectionArgs = arrayOf(taskID.toString())

        val cursor: Cursor =
            database.query(TaskDbHelper.TABLE_NAME, null,  selection,  selectionArgs, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_ID)
            val nameIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_NAME)
            val descIndex: Int = cursor.getColumnIndex(TaskDbHelper.DESCRIPTION)
            do {
                val taskId = cursor.getInt(idIndex)
                val taskName = cursor.getString(nameIndex)
                val taskDesc = cursor.getString(descIndex)
                taskData = (
                    TaskData(
                        task_id = taskId,
                        task_name = taskName,
                        task_description = taskDesc
                    )
                )
            } while (cursor.moveToNext())
        } else taskData = null
        cursor.close()
        dbHelper.close()

        return taskData
    }


    companion object {
        fun newInstance(task_id: Int): TasksViewFragment {
            val args = Bundle()
            args.putInt("task_id", task_id)
            val fragment = TasksViewFragment()
            fragment.arguments = args
            return fragment
        }
    }

}