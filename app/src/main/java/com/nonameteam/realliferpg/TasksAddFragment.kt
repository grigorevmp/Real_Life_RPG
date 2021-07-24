package com.nonameteam.realliferpg

import android.content.ContentValues
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
import com.google.android.material.snackbar.Snackbar
import com.nonameteam.realliferpg.helpers.TaskDbHelper

class TasksAddFragment: Fragment() {
    private var taskName: EditText? = null
    private var tasksDescription: EditText? = null
    private lateinit var dbHelper: TaskDbHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_add, container, false)


        taskName = view.findViewById(R.id.task_name)
        tasksDescription = view.findViewById(R.id.task_description)

        val addTaskButton = view.findViewById<FloatingActionButton>(R.id.add_task)

        addTaskButton.setOnClickListener { addTask(view)}

        dbHelper = TaskDbHelper(view.context)

        return view
    }

    private fun addTask(v: View) {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues()

        if (taskName != null) {
            val taskNameForDB = taskName!!.text.toString()
            val taskDescForDB = tasksDescription!!.text.toString()


            contentValues.put(TaskDbHelper.TASK_NAME, taskNameForDB)
            if(taskDescForDB != "")
                contentValues.put(TaskDbHelper.DESCRIPTION, taskDescForDB)
            database.insert(TaskDbHelper.TABLE_NAME, null, contentValues)
            dbHelper.close()
        }
        else {
            // TODO Нормальные ворнинге
            val snackbar = Snackbar.make(
                v, "No name of the task",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            //snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            //snackbarView.setBackgroundColor(Color.LTGRAY)
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            //textView.setTextColor(Color.BLUE)
            //textView.textSize = 28f
            snackbar.show()
        }
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit();

    }

}