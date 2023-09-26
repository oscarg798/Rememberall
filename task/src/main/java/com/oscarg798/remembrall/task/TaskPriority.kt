package com.oscarg798.remembrall.task

sealed class TaskPriority : Comparable<TaskPriority> {

    object Urgent : TaskPriority() {

        override fun compareTo(other: TaskPriority): Int {
            return when (other) {
                High -> Greater
                Low -> Greater
                Medium -> Greater
                Urgent -> Equals
            }
        }
    }

    object High : TaskPriority() {

        override fun compareTo(other: TaskPriority): Int {
            return when (other) {
                High -> Equals
                Low -> Greater
                Medium -> Greater
                Urgent -> Lower
            }
        }
    }

    object Medium : TaskPriority() {
        override fun compareTo(other: TaskPriority): Int {
            return when (other) {
                High -> Lower
                Low -> Greater
                Medium -> Equals
                Urgent -> Lower
            }
        }
    }

    object Low : TaskPriority() {
        override fun compareTo(other: TaskPriority): Int {
            return when (other) {
                High -> Lower
                Low -> Equals
                Medium -> Lower
                Urgent -> Lower
            }
        }
    }

    companion object {

        fun values() = TaskPriority::class.nestedClasses
            .map { klass -> klass.objectInstance }
            .filterIsInstance<TaskPriority>()

        fun fromName(priority: String): TaskPriority? = values().firstOrNull {
            it.javaClass.name == priority
        }
    }
}

private const val Equals = 0
private const val Greater = -1
private const val Lower = 1
