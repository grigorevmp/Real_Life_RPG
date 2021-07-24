package com.nonameteam.realliferpg

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
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


class TasksFragment: Fragment() {
    private var taskData: MutableList<TaskData> = ArrayList()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        spawnChipGroup(view)

        val gd = GridLayoutManager(view.context, 1)
        recycler = view.findViewById(R.id.task_recycler_view)
        val recyclerEmpty = view.findViewById<TextView>(R.id.recyclerEmpty)

        if (savedInstanceState == null) {
            initDataSource(view)
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
        swipeContainer.setOnRefreshListener {
            Handler().postDelayed({
                onTaskChanged(taskData)
                swipeContainer.isRefreshing = false
            }, 2000)
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

    // TODO Generate chips by database query
    private fun spawnChipGroup(view: View) {
        val chip = Chip(view.context)
        chip.text = "Test1"
        //chip.setChipBackgroundColorResource(R.color.black)
        chip.isCloseIconVisible = true
        //chip.setTextColor(resources.getColor(R.color.white))
        chip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Chip)

        val chip2 = Chip(view.context)
        chip2.text = "Test2" //chip2

       // chip2.setChipBackgroundColorResource(R.color.black)
        chip2.isCloseIconVisible = true
        //chip2.setTextColor(resources.getColor(R.color.white))
        chip2.setTextAppearance(R.style.TextAppearance_MaterialComponents_Chip)


        val chipGroup: ChipGroup = view.findViewById(R.id.taskProjectChipGroup)

        chipGroup.addView(chip)
        chipGroup.addView(chip2)
    }


}
