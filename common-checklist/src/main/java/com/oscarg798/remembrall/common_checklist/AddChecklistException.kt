package com.oscarg798.remembrall.common_checklist

sealed class AddChecklistException(
    override val message: String? = null,
    override val cause: Exception? = null
) : Exception(message, cause) {

    class NameValidationError : AddChecklistException()
    class CheckListItemError : AddChecklistException()
    class CheckListCanNotBeEmptyError: AddChecklistException()
    class IconMissedError: AddChecklistException()
    class AddError(cause: Exception? = null) : AddChecklistException(cause = cause)
    class RollbackError(id: String, cause: Exception? = null) :
        AddChecklistException("Error rolling back checklist $id")
    class UnableToFindChecklists(cause: Exception? = null) :
        AddChecklistException(cause = cause)
    class ChecklistNotFoundById(id: String, cause: Exception? = null): AddChecklistException(
        message = "Checklist not found $id", cause = cause)
}