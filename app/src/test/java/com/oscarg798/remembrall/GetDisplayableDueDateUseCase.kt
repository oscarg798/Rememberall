package com.oscarg798.remembrall

import com.oscarg798.remembrall.addtask.usecase.FormatDueDateImpl
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.Test

class GetDisplayableDueDateUseCase {

    @Test
    fun `given localdate when usecase executed then it should return displayable date`() {
        val formatter: DueDateFormatterImpl = mockk()
        every { formatter.toDisplayableDate(LocalDateTime.MIN) } answers { "1" }
        val usecase = FormatDueDateImpl(formatter)

        assert(usecase.execute(LocalDateTime.MIN) == "1")
    }
}
