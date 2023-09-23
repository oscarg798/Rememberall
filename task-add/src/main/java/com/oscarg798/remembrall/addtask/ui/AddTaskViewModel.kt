package com.oscarg798.remembrall.addtask.ui

import androidx.lifecycle.ViewModel
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.addtask.R
import com.oscarg798.remembrall.addtask.domain.Event
import com.oscarg798.remembrall.addtask.exception.AddTaskException
import com.oscarg798.remembrall.addtask.usecase.AddTaskUseCase
import com.oscarg798.remembrall.addtask.usecase.GetDisplayableDueDate
import com.oscarg798.remembrall.common.model.TaskPriority
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.viewmodel.AbstractViewModel
import com.oscarg798.remembrall.common.viewmodel.launch
import com.oscarg798.remembrall.common_addedit.usecase.GetAvailablePrioritiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@HiltViewModel
internal class AddTaskViewModel @Inject constructor(
    private val getFormattedDueDate: GetDisplayableDueDate,
    private val addTaskUseCase: AddTaskUseCase,
    private val getAvailablePrioritiesUseCaseEdit: GetAvailablePrioritiesUseCase,
    private val stringProvider: StringProvider,
    coroutineContextProvider: CoroutineContextProvider
) : ViewModel(), CoroutineContextProvider by coroutineContextProvider {

    fun onEvent(event: Event){
       //
    }
}