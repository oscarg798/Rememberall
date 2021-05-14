#!/usr/bin/env kotlin

@file:Import("SlackMessage.main.kts")

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object Hermes {

    fun display(message: String) {
        println("##################################################")
        println(message)
    }

    fun notify(
        slackMessage: SlackMessage.Message,
        webHookUrl: String
    ) {
        val url = URL(webHookUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.doOutput = true
        connection.requestMethod = NotificationMethod
        connection.setRequestProperty("Content-Type", NotificationContentType)

        connection.outputStream.write(
            slackMessage.toString().trimIndent()
                .toByteArray(StandardCharsets.UTF_8)
        )
        connection.connect()

        val responseReader =
            BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
        val sb = StringBuilder()

        var readChar: Int
        while (responseReader.read().also { readChar = it } >= 0) {
            sb.append(readChar.toChar())
        }

        val response = sb.toString()

        display("Notification Response : $response")
    }


    private const val NotificationMethod = "POST"
    private const val NotificationContentType = "application/json"
}