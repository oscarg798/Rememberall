object SlackMessage {

    data class Message(val blocks: List<Block>) {
        override fun toString(): String {
            return """
            { 
                "blocks":[${parseBlocks()}] 
             
            }
        """.trimIndent()
        }

        private fun parseBlocks() = blocks.map { it.toString() }.joinToString(",")
    }

    sealed class ButtonStyle(val name: String) {

        object Primary : ButtonStyle(name = "primary")
        object Danger : ButtonStyle(name = "danger")
    }

    sealed class TextField(val text: String, val type: String) {

        class PlainText(text: String) : TextField(text, "plain_text")
        class Markdown(text: String) : TextField(text, "mrkdwn")

        override fun toString(): String {
            return """
               {
                    "type": "$type",
                    "text": "$text"
               }
        """.trimIndent()
        }
    }

    data class Button(
        val style: ButtonStyle,
        val text: TextField.PlainText,
        val url: String? = null
    ) {

        override fun toString(): String = """
            {
                "type": "button",
                "text": $text,
                "style": "${style.name}",
                ${getURl()}
            }
        """.trimIndent()

        private fun getURl() = if (url != null) {
            """
                "url": "$url"
            """.trimIndent()
        } else {
            ""
        }
    }

    sealed class Block() {

        class Text(val message: TextField) : Block() {
            override fun toString(): String {
                return """
                {
                    "type": "section",
                    "text": $message
                }
            """.trimIndent()
            }
        }

        object Divier : Block() {

            override fun toString(): String {
                return """
                {
                    "type": "divider"
                }
            """.trimIndent()
            }
        }

        data class TextFieldSection(val fields: List<TextField>) : Block() {

            override fun toString(): String {
                return """
                {
                    "type": "section",
                    "fields": [${parseFields()}]
                }
            """.trimIndent()
            }

            private fun parseFields() = fields.map { it.toString() }.joinToString(",")
        }

        data class Actions(val buttons: List<Button>) : Block() {

            override fun toString(): String = """
            {
                    "type": "actions",
                    "elements": [${parseButtons()}]
            }
        """.trimIndent()

            private fun parseButtons() = buttons.map { it.toString() }.joinToString(",")
        }
    }
}

