package com.nonameteam.realliferpg.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private val DATABASE_NAME = "realliferpg_tasks.db"
        const val TABLE_NAME = "task_table"
        // Parameters
        const val TASK_ID = "task_id"
        const val TASK_NAME = "task_name"
        const val DESCRIPTION = "description"
        const val PRIORITY = "priority"
        const val DATE = "date"
        const val NOTIFY = "notify"
        const val REPEAT_TIME = "repeat_time"
        const val REPEAT_COUNT = "repeat_count"
        const val PROJECT = "project"
        const val TAGS = "tags"
        const val SKILL = "skill"
        const val COMPLETE = "complete"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASK_NAME + " TEXT UNIQUE NOT NULL," +
                DESCRIPTION + " TEXT," +
                PRIORITY + " TEXT," +
                DATE + " TEXT," +
                NOTIFY + " BOOLEAN," +
                REPEAT_TIME + " TEXT," +
                REPEAT_COUNT + " TEXT," +
                PROJECT + " TEXT," +
                TAGS + " TEXT," +
                SKILL + " TEXT," +
                COMPLETE + " BOOLEAN NOT NULL" +
                ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(this)
        }
    }

    fun addTask(
        taskName: String,
        task_description: String? = null,
        task_priority: String? = null,
        task_date: String? = null,
        task_notify: Boolean = false,
        task_repeat_time: String? = null,
        task_repeat_count: String? = null,
        project: String? = null,
        tags: String? = null,
        skill: String? = null,
        complete: Boolean = false,
    ) {
        val values = ContentValues()
        values.put(TABLE_NAME, taskName)
        if (task_description != null)
            values.put(DESCRIPTION, task_description)
        if (task_priority != null)
            values.put(PRIORITY, task_priority)
        if (task_date != null)
            values.put(DATE, task_date)
        values.put(NOTIFY, task_notify)
        if (task_repeat_time != null)
            values.put(REPEAT_TIME, task_repeat_time)
        if (task_repeat_count != null)
            values.put(REPEAT_COUNT, task_repeat_count)
        if (project != null)
            values.put(PROJECT, project)
        if (tags != null)
            values.put(TAGS, tags)
        if (skill != null)
            values.put(SKILL, skill)
        values.put(COMPLETE, complete)
        
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTasks(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

}