package com.oscarg798.remembrall

sealed class TaskPriority {

    object Urgent: TaskPriority()
    object High : TaskPriority()
    object Medium: TaskPriority()
    object Low: TaskPriority()

}