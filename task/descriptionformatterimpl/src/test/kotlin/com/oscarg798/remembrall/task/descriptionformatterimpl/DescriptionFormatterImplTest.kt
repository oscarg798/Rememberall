package com.oscarg798.remembrall.task.descriptionformatterimpl

import com.google.common.truth.Truth.assertThat
import com.oscarg798.rememberall.task.descriptionformatter.Description
import com.oscarg798.rememberall.task.descriptionformatter.Description.Format
import java.text.Normalizer.Form
import org.junit.Test

internal class DescriptionFormatterImplTest {

    private val underTest = DescriptionFormatterImpl()

    @Test
    fun givenAnEmptyString_whenInvoked_thenReturnRightDescription() {
        val response = underTest.invoke("")

        assertThat(response).isEqualTo(Description(emptyList()))
        assertThat(response.toString()).isEqualTo("")
    }

    @Test
    fun givenNoEmptyString_whenInvoked_thenReturnRightDescription() {
        val response = underTest.invoke("osc")

        assertThat(response).isEqualTo(Description(listOf(Format.UnFormatted("osc"))))
        assertThat(response.toString()).isEqualTo("osc")
    }

    @Test
    fun givenNoEmptyStringWithSingleSpaces_whenInvoked_thenReturnRightDescription() {
        val response = underTest.invoke("oscar david")

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted("oscar david")
                )
            )
        )
        assertThat(response.toString()).isEqualTo("oscar david")
    }

    @Test
    fun givenNoEmptyStringWithMultipleSpaces_whenInvoked_thenReturnRightDescription() {
        val value = "  oscar    david   "

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted(value),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenStringContainingInitialBoldToken_whenInvoked_thenReturnRightDescription() {
        val value = "oscar da*vid no bold "

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted(value),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenStringContainingBoldTokens_whenInvoked_thenReturnRightDescription() {
        val value = "*oscar*"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.Bold("oscar"),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenStringContainingBoldTokensWithSpaces_whenInvoked_thenReturnRightDescription() {
        val value = " david  *pesd* pero"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted(" david  "),
                    Format.Bold("pesd"),
                    Format.UnFormatted(" pero"),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAnStringWithBoldTokenButSpaced_whenInvoked_thenReturnRightDescription() {
        val value = " *da * vid* "

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted(" "),
                    Format.Bold("da * vid"),
                    Format.UnFormatted(" "),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAnStringWithMultipleBoldChains_whenInvoked_thenReturnRightDescription(){
        val value = " *oscar* *da *vid* *gallon* *rosero"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted(" "),
                    Format.Bold("oscar"),
                    Format.UnFormatted(" "),
                    Format.Bold("da *vid"),
                    Format.UnFormatted(" "),
                    Format.Bold("gallon"),
                    Format.UnFormatted(" *rosero"),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

}