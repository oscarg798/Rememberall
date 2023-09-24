package com.oscarg798.remembrall.dateformatter

import java.time.LocalDateTime

interface DueDateFormatter {
    fun toDisplayableDate(dueDate: Long, short: Boolean = false): String
    fun toDisplayableDate(date: LocalDateTime, short: Boolean = false): String
    fun toDueDateInMillis(dueDate: String): Long
    fun toDueDateInMillis(date: LocalDateTime): Long
    fun toDueLocalDatetime(date: Long): LocalDateTime
    fun toCalendarTaskDate(date: Long): String
    fun toCalendarTaskDate(date: LocalDateTime): String
    fun getYearFromDueDate(dueDate: Long): String
    fun getDayFromDueDate(dueDate: Long): String
    fun getDayNameFromDueDate(dueDate: Long): String
    fun getMonthNumber(monthName: String): String
    fun getMonthFromDueDate(dueDate: Long): String
}