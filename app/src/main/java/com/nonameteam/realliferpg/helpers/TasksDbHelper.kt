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
        const val SKILL = "skill"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASK_NAME + " TEXT UNIQUE NOT NULL," +
                DESCRIPTION + " TEXT," +
                PRIORITY + " TEXT," +
                DATE + " TEXT," +
                NOTIFY + " TEXT," +
                REPEAT_TIME + " TEXT," +
                REPEAT_COUNT + " TEXT," +
                PROJECT + " TEXT," +
                SKILL + " TEXT" +
                ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        with(db) {
            execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(this)
        }
    }

    fun addTask(taskName: String) {
        val values = ContentValues()
        values.put(TABLE_NAME, taskName)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

}