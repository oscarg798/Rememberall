package com.oscarg798.rememberall.task.descriptionformatter

data class Description(
    val format: List<Format>
) {

    override fun toString(): String {
        return format.joinToString("") { it.toString() }
    }

    override fun equals(other: Any?): Boolean {
        return other is Description && other.toString() == toString() &&
                other.format == format
    }

    override fun hashCode(): Int {
        return format.hashCode()
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

        data object LineBreak : Format("\n"){

            override fun toString(): String  = value
        }

        object Bullet: Format("* "){
            override fun toString(): String  = value
        }
    }
}
