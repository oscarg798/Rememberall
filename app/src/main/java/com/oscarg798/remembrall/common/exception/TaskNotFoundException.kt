package com.oscarg798.remembrall.common.exception

class TaskNotFoundException(id: String) :
    NullPointerException("We were not able to find a task with id $id")
