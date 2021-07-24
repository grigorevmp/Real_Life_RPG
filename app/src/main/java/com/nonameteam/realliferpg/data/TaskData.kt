package com.nonameteam.realliferpg.data

data class TaskData (
    val task_id: Int,
    val task_name: String,
    val task_description: String? = null,
    val task_priority: String? = null,
    val task_date: String? = null,
    val task_notify: Boolean = false,
    val task_repeat_time: String? = null,
    val task_repeat_count: String? = null,
    val project: String? = null,
    val tags: String? = null,
    val skill: String? = null,
    val complete: Boolean = false,
        )