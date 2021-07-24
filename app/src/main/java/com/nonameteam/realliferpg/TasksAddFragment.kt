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
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nonameteam.realliferpg.helpers.TaskDbHelper

class TasksAddFragment: Fragment() {
    private var taskName: EditText? = null
    private var tasksDescription: EditText? = null
    private var tasksProject: EditText? = null
    private var tasksTags: EditText? = null
    private var tasksSkills: EditText? = null
    private var tasksRepeat: EditText? = null
    private var priority: Int = 4
    private lateinit var dbHelper: TaskDbHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_add, container, false)


        taskName = view.findViewById(R.id.task_name)
        tasksDescription = view.findViewById(R.id.task_description)
        tasksProject = view.findViewById(R.id.task_project)
        tasksTags = view.findViewById(R.id.task_tags_project)
        tasksSkills = view.findViewById(R.id.task_skills)
        tasksRepeat = view.findViewById(R.id.task_repeat_num)

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
            val taskProjectForDB = tasksProject!!.text.toString()
            val taskTagsForDB = tasksTags!!.text.toString()
            val taskSkillsForDB = tasksSkills!!.text.toString()
            val repeatTime = tasksRepeat!!.text.toString()

            val prior1 = v.findViewById<Chip>(R.id.priorityChip1)
            val prior2 = v.findViewById<Chip>(R.id.priorityChip2)
            val prior3 = v.findViewById<Chip>(R.id.priorityChip3)
            val day1 = v.findViewById<Chip>(R.id.dayChip1)
            val day2 = v.findViewById<Chip>(R.id.dayChip2)
            val day3 = v.findViewById<Chip>(R.id.dayChip3)
            val day4 = v.findViewById<Chip>(R.id.dayChip4)
            val day5 = v.findViewById<Chip>(R.id.dayChip5)
            val day6 = v.findViewById<Chip>(R.id.dayChip6)
            val day7 = v.findViewById<Chip>(R.id.dayChip7)

            when {
                prior1.isChecked -> {
                    priority = 1
                }
                prior2.isChecked -> {
                    priority = 2
                }
                prior3.isChecked -> {
                    priority = 3
                }
            }

            contentValues.put(TaskDbHelper.TASK_NAME, taskNameForDB)
            if(taskDescForDB != "")
                contentValues.put(TaskDbHelper.DESCRIPTION, taskDescForDB)
            if(taskProjectForDB != "")
                contentValues.put(TaskDbHelper.PROJECT, taskProjectForDB)
            if(taskTagsForDB != "")
                contentValues.put(TaskDbHelper.TAGS, taskTagsForDB)
            if(taskSkillsForDB != "")
                contentValues.put(TaskDbHelper.SKILL, taskSkillsForDB)
            if(repeatTime != "")
                contentValues.put(TaskDbHelper.REPEAT_COUNT, repeatTime)
            contentValues.put(TaskDbHelper.COMPLETE, false)
            contentValues.put(TaskDbHelper.PRIORITY, priority)
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