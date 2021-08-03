package com.oscarg798.remembrall.addtask.exception

sealed class AddTaskException : IllegalArgumentException() {

    object MissingName : AddTaskException()
    object MissingPriority : AddTaskException()
    object AttendeesWrongFormat : AddTaskException()
    object AttendeesRequiredDueDate : AddTaskException()
}
