#!/usr/bin/env kotlin

@file:Import("Hermes.main.kts")

import java.util.concurrent.TimeUnit

fun createBranch(branchName: String, source: String, repository: String) {
    var output = ""

    Hermes.display("checking out $source")

    output += exec("git checkout $source")

    Hermes.display("pulling source branch")

    output += exec("git pull $repository")

    Hermes.display("creating release branch: $branchName")

    output += exec("git checkout -b $branchName")

    Hermes.display("branch created")

    Hermes.display("pushing branch $repository")

    output += exec("git push $repository -u")

    Hermes.display("branch pushed")

    Hermes.display(output)
}

fun exec(command: String) {
    val arguments = command.split(' ').toTypedArray()
    return execute(*arguments)
}

fun execute(vararg arguments: String) {
    val process = ProcessBuilder(*arguments)
        .start()
        .also { it.waitFor(5, TimeUnit.MINUTES) }

    if (process.exitValue() != 0) {
        throw IllegalStateException(process.errorStream.bufferedReader().readText())
    }

    process.inputStream.bufferedReader().forEachLine { println(it) }
}
