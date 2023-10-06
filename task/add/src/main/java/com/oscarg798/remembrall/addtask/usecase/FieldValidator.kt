package com.oscarg798.remembrall.addtask.usecase

import java.util.regex.Pattern
import javax.inject.Inject

internal interface FieldValidator {

    fun isTitleValid(title: String): Boolean

    fun areAttendeesValid(attendees: Set<String>): Boolean
}

internal class FieldValidatorImpl @Inject constructor(
    private val emailPattern: Pattern
) : FieldValidator {

    override fun areAttendeesValid(attendees: Set<String>): Boolean {
        return attendees.count {
            !emailPattern.matcher(it).matches()
        } == 0
    }

    override fun isTitleValid(title: String): Boolean {
        return title.length >= RequiredNameLength
    }
}

private const val RequiredNameLength = 3