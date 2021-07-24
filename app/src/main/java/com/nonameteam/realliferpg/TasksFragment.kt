package com.nonameteam.realliferpg

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nonameteam.realliferpg.helpers.TaskDbHelper


class TasksFragment: Fragment() {
    private var taskName: EditText? = null
    private var tasksContent: TextView? = null
    private lateinit var dbHelper: TaskDbHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_tasks, container, false)
        taskName = view.findViewById(R.id.taskName)
        tasksContent = view.findViewById(R.id.tasksContent)

        val add_task_button = view.findViewById<Button>(R.id.add_task_button)
        val read_task_button = view.findViewById<Button>(R.id.read_task_button)

        add_task_button.setOnClickListener { onClick(add_task_button)}
        read_task_button.setOnClickListener { onClick(read_task_button)}

        dbHelper = TaskDbHelper(view.context)

        return view
    }

    private fun onClick(v: View) {
        val name: String = taskName!!.text.toString()
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues()
        when (v.id) {
            R.id.add_task_button -> {
                contentValues.put(TaskDbHelper.TASK_NAME, name)
                database.insert(TaskDbHelper.TABLE_NAME, null, contentValues)
            }
            R.id.read_task_button -> {
                val cursor: Cursor =
                    database.query(TaskDbHelper.TABLE_NAME, null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    val idIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_ID)
                    val nameIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_NAME)
                    do {
                        tasksContent!!.text = "${tasksContent!!.text} ${cursor.getString(nameIndex)}"
                    } while (cursor.moveToNext())
                } else tasksContent!!.text = "0 rows"
                cursor.close()
            }
            /*R.id.btnClear -> database.delete(DBHelper.TABLE_CONTACTS, null, null)*/
        }
        dbHelper.close()
    }
}
