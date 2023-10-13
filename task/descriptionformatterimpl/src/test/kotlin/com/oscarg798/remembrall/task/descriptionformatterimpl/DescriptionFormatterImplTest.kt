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

    @Test
    fun givenAnStringWithLineBreak_whenInvoked_thenRightDescriptionReturned(){
        val value = "Given an string with line *break just* return \n the proper *string*"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted("Given an string with line "),
                    Format.Bold("break just"),
                    Format.UnFormatted(" return "),
                    Format.LineBreak,
                    Format.UnFormatted(" the proper "),
                    Format.Bold("string"),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAnStringWithLineBreaks_whenInvoked_thenRightDescriptionReturned(){
        val value = "this must\n work \n is\nmultiple\n *lines\n*"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.UnFormatted("this must"),
                    Format.LineBreak,
                    Format.UnFormatted(" work "),
                    Format.LineBreak,
                    Format.UnFormatted(" is"),
                    Format.LineBreak,
                    Format.UnFormatted("multiple"),
                    Format.LineBreak,
                    Format.UnFormatted(" *lines"),
                    Format.LineBreak,
                    Format.UnFormatted("*"),
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAStringWithBulletPoints_whenInvoked_thenRightDescriptionReturned(){
        val value = "* this has a bullet point\n"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.Bullet,
                    Format.UnFormatted("this has a bullet point"),
                    Format.LineBreak
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAStringWithBulletPointsAndBold_whenInvoked_thenRightDescriptionReturned(){
        val value = "* this has *a bullet* point\n"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.Bullet,
                    Format.UnFormatted("this has "),
                    Format.Bold("a bullet"),
                    Format.UnFormatted(" point"),
                    Format.LineBreak
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

    @Test
    fun givenAStringWithMultipleLines_whenInvoked_thenRightDescriptionReturned(){
        val value = "* this is a bullet line\n* same for this line\n but this one"

        val response = underTest.invoke(value)

        assertThat(response).isEqualTo(
            Description(
                listOf(
                    Format.Bullet,
                    Format.UnFormatted("this is a bullet line"),
                    Format.LineBreak,
                    Format.Bullet,
                    Format.UnFormatted("same for this line"),
                    Format.LineBreak,
                    Format.UnFormatted(" but this one")
                )
            )
        )
        assertThat(response.toString()).isEqualTo(value)
    }

}