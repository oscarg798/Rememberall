package com.oscarg798.remembrall.dateformatter

import java.time.LocalDateTime

interface DateFormatter {
    fun toDisplayableDate(date: Long, short: Boolean = false): String
    fun toDisplayableDate(date: LocalDateTime, short: Boolean = false): String
    fun toMillis(date: String): Long
    fun toMillis(date: LocalDateTime): Long
    fun toLocalDatetime(date: Long): LocalDateTime
    fun toCalendarTaskDate(date: Long): String
    fun toCalendarTaskDate(date: LocalDateTime): String
    fun getYearFromDate(dateInMillis: Long): String
    fun getDayFromDate(dateInMillis: Long): String
    fun getDayNameFromDate(date: Long): String
    fun getMonthNumber(monthName: String): String
    fun getMonthFromDate(date: Long): String
}