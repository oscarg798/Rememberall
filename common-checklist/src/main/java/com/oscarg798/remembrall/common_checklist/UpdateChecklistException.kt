package com.oscarg798.remembrall.common_checklist

sealed class UpdateChecklistException(
    override val message: String? = null,
    override val cause: Exception? = null
) : Exception(message, cause) {


    class UnableToUpdateChecklist(id: String, cause: Exception? = null) :
        UpdateChecklistException(message = "Unable to update checklist $id", cause = cause)

    class ErrorUpdatingChecklistItem(
        checklistId: String,
        checklistItemId: String,
        cause: Exception? = null
    ) : UpdateChecklistException(message = "Unable to update checklist $checklistId item $checklistItemId", cause = cause)
}

