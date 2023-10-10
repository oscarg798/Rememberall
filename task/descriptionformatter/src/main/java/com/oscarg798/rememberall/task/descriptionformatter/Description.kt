package com.oscarg798.rememberall.task.descriptionformatter

data class Description(
    val value: List<Format>
) {

    override fun toString(): String {
        return value.joinToString("") { it.toString() }
    }

    override fun equals(other: Any?): Boolean {
        return other is Description && other.toString() == toString() &&
                other.value == value
    }

    sealed class Format(open val value: String) {

        data class Bold(override val value: String) : Format(value) {
            override fun toString(): String {
                return "*$value*"
            }
        }

        data class UnFormatted(override val value: String) : Format(value) {
            override fun toString(): String {
                return value
            }
        }
    }
}
