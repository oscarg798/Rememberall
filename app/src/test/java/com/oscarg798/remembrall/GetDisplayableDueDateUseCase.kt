package com.oscarg798.remembrall

import com.oscarg798.remembrall.addtask.usecase.GetDisplayableDueDate
import com.oscarg798.remembrall.common.formatters.DueDateFormatter
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.time.LocalDateTime

class GetDisplayableDueDateUseCase {

    @Test
    fun `given localdate when usecase executed then it should return displayable date`() {
        val formatter: DueDateFormatter = mockk()
        every { formatter.toDisplayableDate(LocalDateTime.MIN) } answers { "1" }
        val usecase = GetDisplayableDueDate(formatter)

        assert(usecase.execute(LocalDateTime.MIN) == "1")
    }
}