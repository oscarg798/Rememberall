package com.oscarg798.remembrall.common_task

sealed class TaskDataSourceException(
    message: String? = null,
    cause: Exception? = null
) : Exception(message, cause) {

    class UnableToAddTask(cause: Exception? = null) : TaskDataSourceException(cause = cause)
    class UnableToLoadTasks(cause: Exception? = null) : TaskDataSourceException(cause = cause)
    class UnableToUpdateTask(taskId: String, cause: Exception? = null) :
        TaskDataSourceException(cause = cause, message = "Unable to update task $taskId")

    class UnableToDeleteTask(taskId: String, cause: Exception? = null) :
        TaskDataSourceException(cause = cause, message = "Unable to update task $taskId")
}
