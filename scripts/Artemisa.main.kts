#!/usr/bin/env kotlin

@file:Import("Hermes.main.kts")
@file:Import("Version.kts")
@file:Import("Hades.kts")

import java.nio.file.Paths

val WebHookKey = "--webhook"
val CommandKey = "--command"
val ParentPath = Paths.get(System.getProperty("user.dir")).parent.toAbsolutePath().toString()
val GradleWrapperPath = "$ParentPath/gradlew"
val TimeOut = 10L
val CurrentVersion = Version.createFromProperties()
val RepositoryUriAppendix = "github.com/oscarg798/Remembrall"

if (!args.contains(WebHookKey) || !args.contains(CommandKey)) {
    val usage = """
        Use this tool to run a gradle task easily and notify the result to slack
        Usage: ./Artemisa.main.kts --webhook <webhook_url>
        
        Options:
         --command      Command to run such as assembleRelease    
         --webhook      Slack webhook url for notification purposes
    """

    throw IllegalStateException(usage)
}

val webhookUrl = getArgument(WebHookKey)
val command = getArgument(CommandKey)

Hermes.display("$GradleWrapperPath $command")

runCatching {
    Hades.exec(
        Hades.Command(
            command = "$GradleWrapperPath $command",
            path = ParentPath,
            commandOutputListener = object : Hades.CommandOutputListener {
                override fun onOutput(output: String) {
                    Hermes.display(output)
                }
            }, timeOut = TimeOut
        )
    )

}.onSuccess {
    Hermes.notify(
        messageFormat = Hermes.MessageFormat(
            message = "Command ran successfully ",
            sectionBlockText = "**Awesome**",
            buttonStyle = Hermes.ButtonStyle.Primary(
                placeholder = "Check it "
            )
        ),
        webHookUrl = webhookUrl,
        actionUrl = "https://$RepositoryUriAppendix/actions"
    )

}.onFailure { error ->
    Hermes.notify(
        messageFormat = Hermes.MessageFormat(
            message = "Error running command $command",
            sectionBlockText = "@channel check the output ${error.message ?: error}",
            buttonStyle = Hermes.ButtonStyle.Danger(
                placeholder = "Fix it"
            )
        ),
        webHookUrl = webhookUrl,
        actionUrl = "https://$RepositoryUriAppendix/actions"
    )
}

fun getArgument(argumentKey: String): String {
    return if (args.contains(argumentKey)) {
        args[1 + args.indexOf(argumentKey)]
    } else {
        throw IllegalArgumentException("Argument $argumentKey not found")
    }
}

