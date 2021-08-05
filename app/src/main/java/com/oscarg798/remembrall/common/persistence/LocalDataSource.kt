package com.oscarg798.remembrall.common.persistence

// class LocalDataSource @Inject constructor(private val taskDao: TaskDao) : TaskDataSource {
//
//    override suspend fun addTask(taskDto: TaskDto) {
//        taskDao.addTask(taskDto.toEntity())
//    }
//
//    override suspend fun getTask(id: String): TaskDto {
//        return taskDao.find(id)?.toTaskDto() ?: throw TaskNotFoundException(id)
//    }
//
//    override suspend fun getTasks(): Collection<TaskDto> {
//        return taskDao.getAll().map { it.toTaskDto() }
//    }
//
//    override suspend fun deleteTask(id: String) {
//        val task = taskDao.find(id) ?: throw TaskNotFoundException(id)
//        taskDao.update(task.copy(completed = true))
//    }
//
//    override suspend fun update(task: TaskDto) {
//        taskDao.update(task.toEntity())
//    }
//
//    private fun TaskDto.toEntity() = TaskEntity(
//        id = id,
//        name = name, description = description,
//        priority = priority::class.qualifiedName!!.toString(),
//        completed = completed,
//        dueDate = dueDate,
//        calendarSynced = calendarSyncInformation?.synced,
//        calendarEventId = calendarSyncInformation?.calendarEventId,
//        calendarId = calendarSyncInformation?.calendarId,
//        attendees = calendarSyncInformation?.attendees?.map { it.email }
//            ?.joinToString(AttendeesSeparator)
//    )
//
//    private fun TaskEntity.toTaskDto(): TaskDto = TaskDto(
//        id = id,
//        name = name,
//        description = description,
//        priority = TaskPriority.values().first {
//            it::class.qualifiedName!!.toString() == priority
//        },
//        completed = completed,
//        calendarSyncInformation = getCalendarSyncInformation(),
//        dueDate = dueDate
//    )
//
//    private fun TaskEntity.getCalendarSyncInformation(): CalendarSyncInformationDto? {
//        if (calendarSynced == null || calendarEventId == null || calendarId == null) {
//            return null
//        }
//
//        return CalendarSyncInformationDto(
//            calendarId = calendarId,
//            calendarEventId = calendarEventId,
//            synced = calendarSynced,
//            attendees = attendees?.getAttendees()
//        )
//    }
//
//    private fun String.getAttendees() = if (isNullOrEmpty()) {
//        null
//    } else split(AttendeesSeparator).map {
//        CalendarAttendeeDto(email = it)
//    }
// }

private const val AttendeesSeparator = ";"
