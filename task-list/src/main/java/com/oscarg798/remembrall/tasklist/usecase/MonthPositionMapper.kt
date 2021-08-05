package com.oscarg798.remembrall.tasklist.usecase

import javax.inject.Inject

class MonthPositionMapper @Inject constructor() {

    fun fromMonthName(name: String): String = when (name) {
        January -> "00"
        February -> "01"
        March -> "02"
        April -> "03"
        May -> "04"
        June -> "05"
        July -> "06"
        August -> "07"
        September -> "08"
        October -> "09"
        November -> "10"
        else -> "11"
    }

    fun fromMonthValue(value: Int): String = when (value) {
        0 -> January
        1 -> February
        2 -> March
        3 -> April
        4 -> May
        5 -> June
        6 -> July
        7 -> August
        8 -> September
        9 -> October
        10 -> November
        else -> December

    }

}

private const val January = "January"
private const val February = "February"
private const val March = "March"
private const val April = "April"
private const val May = "May"
private const val June = "June"
private const val July = "July"
private const val August = "August"
private const val September = "September"
private const val October = "October"
private const val November = "November"
private const val December = "December"