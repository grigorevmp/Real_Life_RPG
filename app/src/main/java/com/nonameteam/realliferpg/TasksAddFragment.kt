package com.nonameteam.realliferpg

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nonameteam.realliferpg.helpers.TaskDbHelper

class TasksAddFragment: Fragment() {

    private var taskName: EditText? = null
    private var tasksDescription: EditText? = null
    private var tasksProject: EditText? = null
    private var tasksTags: AutoCompleteTextView? = null
    private var tasksSkills: EditText? = null
    private var tagsChipGroup: FlexboxLayout? = null
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

        tagsChipGroup = view.findViewById(R.id.task_tags_container)

        val allTags = listOf("Test") // TODO Put recommended tags here
        val adapter = ArrayAdapter(view.context,
            android.R.layout.simple_dropdown_item_1line,
            allTags)
        tasksTags?.setAdapter(adapter)

        tasksTags!!.setOnAddTagListener {
            tasksTags!!.text = null
        }

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
            //val taskTagsForDB = tasksTags!!.text.toString()
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

            var taskTagsForDB = ""
            tagsChipGroup?.getAllChips()?.forEach {
                taskTagsForDB += "$it "
            }

            contentValues.put(TaskDbHelper.TASK_NAME, taskNameForDB)
            if (taskDescForDB != "")
                contentValues.put(TaskDbHelper.DESCRIPTION, taskDescForDB)
            if (taskProjectForDB != "")
                contentValues.put(TaskDbHelper.PROJECT, taskProjectForDB)
            if (taskTagsForDB != "")
                contentValues.put(TaskDbHelper.TAGS, taskTagsForDB)
            if (taskSkillsForDB != "")
                contentValues.put(TaskDbHelper.SKILL, taskSkillsForDB)
            if (repeatTime != "")
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

    private fun AutoCompleteTextView.setOnAddTagListener(callback: (name: String) -> Unit) {

        setOnItemClickListener { adapterView, _, position, _ ->
            val name = adapterView.getItemAtPosition(position) as String
            callback(name)
        }

        // done pressed
        setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val name = textView.text.toString()
                if(name != "")
                    tagsChipGroup!!.addChip(name, callback)
                callback(name)

                true
            } else false
        }

        doAfterTextChanged { text ->
            if (text != null && text.isEmpty()) {
                return@doAfterTextChanged
            }

            // comma is detected (optional space)
            if (text?.last() == ',') { //  || text?.last() == ' '
                val name = text.substring(0, text.length - 1)

                if(name != "")
                    tagsChipGroup!!.addChip(name, callback)

                callback(name)
            }
        }
    }


    private fun FlexboxLayout.getAllChips(): List<String> {

        val allTaskTags: MutableList<String> = ArrayList()

        val chipViews = (0 until childCount).mapNotNull { index ->
            val view = getChildAt(index)
            if (view is Chip) view else null
        }
        chipViews.forEach { allTaskTags.add(it.text.toString()) }

        return allTaskTags
        }
    }

    fun FlexboxLayout.clearChips() {
        val chipViews = (0 until childCount).mapNotNull { index ->
            val view = getChildAt(index)
            if (view is Chip) view else null
        }
        chipViews.forEach { removeView(it) }
    }

    private fun ViewGroup.addChip(text: String, removeCallback: (message: String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val chip = inflater.inflate(R.layout.view_chip,  null) as Chip
        chip.text = text

        // val layoutParams = test_chip.layoutParams as ViewGroup.MarginLayoutParams
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
        layoutParams.rightMargin = 10
        addView(chip as View, childCount - 1, layoutParams)
        chip.setOnCloseIconClickListener {
            removeView(chip as View)
            removeCallback(chip.text.toString())
        }
    }

