package com.oscarg798.remembrall.task.descriptionformatterimpl

import com.oscarg798.rememberall.task.descriptionformatter.Description
import com.oscarg798.rememberall.task.descriptionformatter.DescriptionFormatter
import java.util.Stack

class DescriptionFormatterImpl : DescriptionFormatter {


    override fun invoke(value: String): Description {
        val formats = mutableListOf<Description.Format>()
        val phraseQueue = mutableListOf<String>()
        value.forEach { char ->
            val character = char.toString()
            if (character.matches(BreakLine.toRegex())) {
                if (phraseQueue.isNotEmpty()) {
                    formats.add(phraseQueue.asFormat())
                }
                phraseQueue.clear()
            } else if (character.matches(BoldToken.toRegex()) &&
                phraseQueue.isNotEmpty() &&
                !phraseQueue.asPhrase().matches(BoldCandidate.toRegex())
            ) {
                val format = phraseQueue.asFormat()
                formats.addFormatAppendingIfUnformatted(format)

                phraseQueue.clear()
                phraseQueue.add(character)
            } else if (character.matches(BoldToken.toRegex()) &&
                (phraseQueue.asPhrase() + character).matches(Bold.toRegex())
            ) {
                phraseQueue.add(character)
                formats.add(
                    Description.Format.Bold(
                        phraseQueue.asPhrase().drop(1).dropLast(1)
                    )
                )
                phraseQueue.clear()
            } else if (character.isNotEmpty()) {
                phraseQueue.add(character)
            }
        }

        if (phraseQueue.isNotEmpty()) {
            val format = phraseQueue.asFormat()
            formats.addFormatAppendingIfUnformatted(format)
        }

        return Description(formats)
    }

    private fun MutableList<Description.Format>.addFormatAppendingIfUnformatted(newFormat: Description.Format) {
        if (newFormat is Description.Format.UnFormatted &&
            isNotEmpty() &&
            last() is Description.Format.UnFormatted
        ) {
            val previous = last() as Description.Format.UnFormatted
            removeLast()
            add(previous.copy(value = previous.value + newFormat.value))
        } else {
            add(newFormat)
        }
    }


    private fun MutableList<String>.asFormat(): Description.Format =
        if (asPhrase().matches(Bold.toRegex())) {
            // we drop the * at start and at end
            Description.Format.Bold(drop(1).dropLast(1).asPhrase())
        } else {
            Description.Format.UnFormatted(asPhrase())
        }

    private fun List<String>.asPhrase() = joinToString("")

    //
    private fun canSpaceBeAdded(index: Int, words: List<String>) = index + 1 < words.size
}

internal const val Space = "\\s"
private const val BreakLine = "\\n"
private const val Bold = "^\\*.+.[^ \\s]+\\*\$"
private const val BoldToken = "\\*"
private const val TokenizedMinLenght = 3
private const val BoldCandidate = "^\\*.{1,}(\\s+|.+).{1,}\$"