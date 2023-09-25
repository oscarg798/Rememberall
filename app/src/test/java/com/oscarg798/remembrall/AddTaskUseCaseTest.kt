package com.oscarg798.remembrall

import com.oscarg798.remembrall.common_calendar.domain.model.Calendar
import com.oscarg798.remembrall.common_calendar.domain.repository.CalendarRepository
import com.oscarg798.remembrall.common_calendar.exception.CalendarNotFoundException
import com.oscarg798.remembrall.addtask.exception.AddTaskException
import com.oscarg798.remembrall.addtask.usecase.AddTaskUseCase

import com.oscarg798.remembrall.task.CalendarAttendee
import com.oscarg798.remembrall.task.CalendarSyncInformation
import com.oscarg798.remembrall.task.Task
import com.oscarg798.remembrall.task.TaskPriority
import com.oscarg798.remembrall.task.TaskRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddTaskUseCaseTest {

    private val dueDateFormatter: DueDateFormatterImpl = mockk()
    private val taskRepository: TaskRepository = mockk()
    private val calendarRepository: CalendarRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val emailPattern: Pattern = mockk()
    private val matchers: Matcher = mockk()

    private val task: Task = mockk()
    private lateinit var usecase: AddTaskUseCase

    @Before
    fun setup() {
        every { emailPattern.matcher(any()) } answers { matchers }

        usecase = AddTaskUseCase(
            dueDateFormatter = dueDateFormatter,
            taskRepository = taskRepository,
            calendarRepository = calendarRepository,
            authRepository = authRepository,
            emailPattern = emailPattern
        )
    }

    @Test(expected = AddTaskException.MissingName::class)
    fun `given params without any values when usecase executed then it should throw MissingName`() {
        runBlockingTest {
            usecase.execute(params)
        }
    }

    @Test(expected = AddTaskException.MissingName::class)
    fun `given params with name not following length any values when usecase executed then it should throw MissingName`() {
        runBlockingTest {
            usecase.execute(params.copy(name = "12"))
        }
    }

    @Test(expected = AddTaskException.MissingPriority::class)
    fun `given params with name and no priority any values when usecase executed then it should throw MissingPriority`() {
        runBlockingTest {
            usecase.execute(params.copy(name = "name"))
        }
    }

    @Test
    fun `given params with name, priority and without attendees when usecas executed then it should be added but not synced`() {
        every { authRepository.getSignedInUser() } answers { mockk() }
        val params = params.copy(name = "name", priority = TaskPriority.Low)
        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    priority = params.priority!!
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }

        coVerify {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    priority = params.priority!!
                )
            )
            authRepository.getSignedInUser()
        }

        coVerify(exactly = 0) {
            calendarRepository.getSelectedCalendar()
            calendarRepository.addTaskToCalendar(any(), any(), any())
            taskRepository.updateWithCalendarInformation(any(), any())
        }
    }

    @Test
    fun `given params with name, priority, description and without attendees when usecase executed then it should be added but not synced`() {
        every { authRepository.getSignedInUser() } answers { mockk() }

        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low
        )

        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }

        coVerify {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!
                )
            )
            authRepository.getSignedInUser()
        }

        coVerify(exactly = 0) {
            calendarRepository.getSelectedCalendar()
            calendarRepository.addTaskToCalendar(any(), any(), any())
            taskRepository.updateWithCalendarInformation(any(), any())
        }
    }

    @Test(expected = AddTaskException.AttendeesWrongFormat::class)
    fun `given params with name, priority, description, wrong attendees when usecase executed then it should throw AttendeesWrongFormat`() {
        every { matchers.matches() } answers { false }
        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low,
            attendees = setOf("12312")
        )
        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }
    }

    @Test(expected = AddTaskException.AttendeesRequiredDueDate::class)
    fun `given params with name, priority, description, attendees but due date when usecase executed then it should throw AttendeesRequiredDueDate`() {
        every { matchers.matches() } answers { true }
        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low,
            attendees = setOf("12312")
        )
        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }
    }

    @Test
    fun `given params with name, priority, description, attendees, user not signed in and due date when usecase executed then it should be added but not synced`() {
        every { dueDateFormatter.toDueDateInMillis(LocalDateTime.MIN) } answers { 1L }
        every { authRepository.getSignedInUser() } answers { throw AuthException.AuthRequired() }
        every { matchers.matches() } answers { true }

        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low,
            attendees = setOf("12312"),
            dueDate = LocalDateTime.MIN
        )

        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }

        coVerify {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
            authRepository.getSignedInUser()
        }

        coVerify(exactly = 0) {
            calendarRepository.getSelectedCalendar()
            calendarRepository.addTaskToCalendar(any(), any(), any())
            taskRepository.updateWithCalendarInformation(any(), any())
        }
    }

    @Test
    fun `given params with name, priority, description, attendees, and due date, user signed in, but not selected calendar when usecase executed then it should be added but not synced`() {
        every { calendarRepository.getSelectedCalendar() } answers { throw CalendarNotFoundException() }
        every { dueDateFormatter.toDueDateInMillis(LocalDateTime.MIN) } answers { 1L }
        every { authRepository.getSignedInUser() } answers { throw AuthException.AuthRequired() }
        every { matchers.matches() } answers { true }

        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low,
            attendees = setOf("12312"),
            dueDate = LocalDateTime.MIN
        )

        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }

        coVerify {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
            authRepository.getSignedInUser()
        }

        coVerify(exactly = 0) {
            calendarRepository.getSelectedCalendar()
            calendarRepository.addTaskToCalendar(any(), any(), any())
            taskRepository.updateWithCalendarInformation(any(), any())
        }
    }

    @Test
    fun `given params with name, priority, description, attendees, and due date, user signed in, selected calendar when usecase executed then it should be added and synced`() {
        every { calendarRepository.getSelectedCalendar() } answers { Calendar("1", "2", true) }
        every { dueDateFormatter.toDueDateInMillis(LocalDateTime.MIN) } answers { 1L }
        every { dueDateFormatter.toCalendarTaskDate(any()) } answers { "ffe" }
        every { authRepository.getSignedInUser() } answers { mockk() }
        every { matchers.matches() } answers { true }
        every { task.id } answers { "1" }
        coEvery { taskRepository.updateWithCalendarInformation(any(), any()) } just Runs

        val calendarInfo = CalendarSyncInformation(
            calendarId = "1",
            calendarEventId = "2",
            synced = true,
            attendees = setOf(CalendarAttendee("123"))
        )

        val params = params.copy(
            name = "name",
            description = "desc",
            priority = TaskPriority.Low,
            attendees = setOf("12312"),
            dueDate = LocalDateTime.MIN
        )

        coEvery {
            calendarRepository.addTaskToCalendar(
                calendarId = "1",
                calendarTask = match {
                    it.summary == "name" && it.description == "desc" && it.id == "1" &&
                        it.startTimeDate == "ffe" && it.endTimeDate == "ffe"
                },
                attendees = setOf("12312")
            )
        } answers { calendarInfo }

        coEvery {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
        } answers { task }

        runBlockingTest {
            usecase.execute(params)
        }

        coVerify {
            taskRepository.addTask(
                TaskRepository.AddTaskParam(
                    name = params.name!!,
                    description = params.description!!,
                    priority = params.priority!!,
                    dueDate = 1L
                )
            )
            authRepository.getSignedInUser()
            calendarRepository.getSelectedCalendar()
            calendarRepository.addTaskToCalendar(
                calendarId = "1",
                calendarTask = match {
                    it.summary == "name" && it.description == "desc" && it.id == "1" &&
                        it.startTimeDate == "ffe" && it.endTimeDate == "ffe"
                },
                attendees = setOf("12312")
            )
            taskRepository.updateWithCalendarInformation(
                tasksCalendarSyncInformation = calendarInfo,
                task = task
            )
        }
    }
}

private val params = AddTaskUseCase.AddTaskParam()
