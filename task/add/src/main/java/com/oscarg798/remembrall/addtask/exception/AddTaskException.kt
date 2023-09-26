package com.oscarg798.remembrall.addtask.exception

sealed class AddTaskException : IllegalArgumentException() {

    object MissingName : AddTaskException()
    object MissingPriority : AddTaskException()
    object MissingDueDate: AddTaskException()
    object AttendeesWrongFormat : AddTaskException()
}
