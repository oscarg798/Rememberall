package com.oscarg798.remembrall

import com.oscarg798.remembrall.addtask.usecase.GetDisplayableDueDate
import com.oscarg798.remembrall.common.formatter.DueDateFormatter
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.Test

class GetDisplayableDueDateUseCase {

    @Test
    fun `given localdate when usecase executed then it should return displayable date`() {
        val formatter: DueDateFormatter = mockk()
        every { formatter.toDisplayableDate(LocalDateTime.MIN) } answers { "1" }
        val usecase = GetDisplayableDueDate(formatter)

        assert(usecase.execute(LocalDateTime.MIN) == "1")
    }
}
