package com.oscarg798.remembrall.task.descriptionformatterimpl

import com.oscarg798.rememberall.task.descriptionformatter.Description
import com.oscarg798.rememberall.task.descriptionformatter.DescriptionFormatter
import javax.inject.Inject

class DescriptionFormatterImpl @Inject constructor() : DescriptionFormatter {

    override fun invoke(value: String): Description {
        val formats = mutableListOf<Description.Format>()
        val phraseQueue = mutableListOf<String>()
        value.forEach { char ->
            val character = char.toString()
            if (character.matches(LineBreak.toRegex())) {
                if (phraseQueue.isNotEmpty()) {
                    val format = phraseQueue.asFormat()
                    formats.addFormatAppendingIfUnformatted(format)
                }
                formats.add(Description.Format.LineBreak)
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


        return Description(appendBullets(formats))
    }

    private fun appendBullets(formats: MutableList<Description.Format>): List<Description.Format> {
        if (formats.isEmpty() || !formats.contains(Description.Format.LineBreak)) return formats

        val lines = mutableListOf<List<Description.Format>>()

        var lastBreakLineIndex = -1
        for (i in 0 until formats.size) {
            if (formats[i] is Description.Format.LineBreak && i != 0) {
                // if we have not found a break line then we must evaluate first format, otherwise the next from the last break line
                val initialLineIndex = if (lastBreakLineIndex == -1) 0 else lastBreakLineIndex + 1
                if (initialLineIndex < formats.size &&
                    formats[initialLineIndex] is Description.Format.UnFormatted &&
                    formats[initialLineIndex].value.matches(Bullet.toRegex())
                ) {
                    // We remove the characters matching the bullet
                    val firstUnFormatted = Description.Format.UnFormatted(
                        formats[initialLineIndex].value.drop(2)
                    )

                    // We add the bullet and the format with its characters
                    lines.add(
                        listOf(Description.Format.Bullet) +
                                listOf(firstUnFormatted) +
                                formats.subList(
                                    fromIndex = initialLineIndex + 1,
                                    // toIndex is exclusive so we try to go to the size or to the next item
                                    toIndex = (i + 1).coerceAtMost(formats.size)
                                )
                    )
                } else {
                    lines.add(
                        formats.subList(
                            fromIndex = initialLineIndex,
                            // toIndex is exclusive so we try to go to the size or to the next item
                            toIndex = (i + 1).coerceAtMost(formats.size)
                        )
                    )
                }
                lastBreakLineIndex = i
            } else if (i == formats.lastIndex) {
                val initialLineIndex = if (lastBreakLineIndex == -1) 0 else lastBreakLineIndex + 1
                lines.add(
                    formats.subList(
                        fromIndex = initialLineIndex,
                        // toIndex is exclusive so we try to go to the size or to the next item
                        toIndex = (i + 1).coerceAtMost(formats.size)
                    )
                )
            }
        }

        return lines.flatten()
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
}

private const val LineBreak = "\\n"
private const val Bold = "^(\\*[^\\s])+.+([^\\s]\\*)\$"
private const val BoldToken = "\\*"
private const val BoldCandidate = "^(\\*[^\\s]).{0,}\$"
private const val Bullet = "^\\*\\s.{0,}\$"