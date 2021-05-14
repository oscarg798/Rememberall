#!/usr/bin/env kotlin

@file:Import("Hermes.main.kts")
@file:Import("Hades.kts")

import java.util.concurrent.TimeUnit
import java.nio.file.Paths

val ParentPath = Paths.get(System.getProperty("user.dir")).parent.toAbsolutePath().toString()
val TimeOutInMinutes = 1L

fun createBranch(branchName: String, source: String, repository: String) {

    Hermes.display("checking out $source")

    Hades.exec(getCommand("git checkout $source"))

    Hermes.display("pulling source branch")

    Hades.exec(getCommand("git pull $repository"))

    Hermes.display("creating release branch: $branchName")

    Hades.exec(getCommand("git checkout -b $branchName\""))

    Hermes.display("branch created")

    Hermes.display("pushing branch $repository")

    Hades.exec(getCommand("git push $repository -u"))

    Hermes.display("branch pushed")
}

fun getCommand(command: String) = Hades.Command(
    command = command,
    path = ParentPath,
    commandOutputListener = object : Hades.CommandOutputListener {
        override fun onOutput(output: String) {
            Hermes.display(output)
        }
    }, timeOut = TimeOutInMinutes
)

