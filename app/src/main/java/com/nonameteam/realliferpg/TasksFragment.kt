package com.nonameteam.realliferpg

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nonameteam.realliferpg.data.TaskData
import com.nonameteam.realliferpg.helpers.TaskDbHelper
import com.nonameteam.realliferpg.tasks.TaskCallback
import kotlinx.coroutines.*


class TasksFragment: Fragment() {
    private var taskData: MutableList<TaskData> = ArrayList()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TasksAdapter
    private lateinit var chipGroup: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        chipGroup = view.findViewById(R.id.taskProjectChipGroup)

        val gd = GridLayoutManager(view.context, 1)
        recycler = view.findViewById(R.id.task_recycler_view)
        val recyclerEmpty = view.findViewById<TextView>(R.id.recyclerEmpty)

        if (savedInstanceState == null) {
            initDataSource(view)
            initTags(view)
        }


        val addNewTask = view.findViewById<FloatingActionButton>(R.id.add_new_task)
        addNewTask.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.fragment_container, TasksAddFragment()
                )?.addToBackStack(null)
                ?.commit()
        }

        val listener: OnTaskItemClickListener = object : OnTaskItemClickListener {
            override fun onTaskItemClick(task: TaskData) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(
                        R.id.fragment_container, TasksViewFragment.newInstance(
                            task.task_id,
                        )
                    )?.addToBackStack(null)
                    ?.commit()
            }
        }

        if(recycler.adapter == null)
            adapter = TasksAdapter(view.context, taskData, listener)
        else{
            onTaskChanged(taskData)
        }
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        recycler.adapter = adapter
        recycler.layoutManager = gd

        if (adapter.itemCount == 0) {
            recycler.visibility = View.GONE
            recyclerEmpty.visibility = View.VISIBLE
        }

        val swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("Coroutines error", "handled $exception")
            lifecycleScope.cancel()
            swipeContainer.isRefreshing = false
        }


        swipeContainer.setOnRefreshListener {
            runBlocking {
                val job = lifecycleScope.launch(handler + Job()) {
                    //delay(2000)
                    onTaskChanged(taskData)
                    swipeContainer.isRefreshing = false
                }
            }
        }

        return view
    }

    private fun onTaskChanged (movies: List<TaskData>){
        val callback = TaskCallback(adapter.tasks, movies)
        val diff = DiffUtil.calculateDiff(callback)
        adapter.tasks = movies
        diff.dispatchUpdatesTo(adapter)

    }

    private fun initDataSource(view: View) {
        val dbHelper = TaskDbHelper(view.context)
        val database: SQLiteDatabase = dbHelper.writableDatabase

        val cursor: Cursor =
            database.query(TaskDbHelper.TABLE_NAME, null, null, null, null, null, null)

        taskData.clear()

        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_ID)
            val nameIndex: Int = cursor.getColumnIndex(TaskDbHelper.TASK_NAME)
            val descIndex: Int = cursor.getColumnIndex(TaskDbHelper.DESCRIPTION)
            do {
                val taskId = cursor.getInt(idIndex)
                val taskName = cursor.getString(nameIndex)
                val taskDesc = cursor.getString(descIndex)
                taskData.add(
                    TaskData(
                        task_id = taskId,
                        task_name = taskName,
                        task_description = taskDesc
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()


    }
    private fun initTags(view: View){
        val dbHelper = TaskDbHelper(view.context)
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val allTaskTags: MutableList<String> = ArrayList()

        val cursor: Cursor =
            database.query(TaskDbHelper.TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            val tagsIndex: Int = cursor.getColumnIndex(TaskDbHelper.TAGS)
            do {
                val taskTags = cursor.getString(tagsIndex)
                val tags = taskTags.split(' ')
                tags.forEach {
                    if (it !in allTaskTags){
                        allTaskTags.add(it)
                    }
                }

            } while (cursor.moveToNext())
        }
        cursor.close()

        spawnChipGroup(allTaskTags, view)

    }

    private fun spawnChipGroup(tags: MutableList<String>, view: View) {
        tags.forEach {
            if(it != "" && it != " ") {
                val chip = Chip(view.context)
                chip.text = it
                chip.minWidth = 100
                chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
                chip.isCloseIconVisible = false
                chip.isCheckable = true
                chip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Chip)
                chipGroup.addView(chip)
            }
        }
    }


}
