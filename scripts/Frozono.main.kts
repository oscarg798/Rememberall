#!/usr/bin/env kotlin

@file:Import("BranchCreator.main.kts")
@file:Import("Version.kts")
@file:Import("Hermes.main.kts")
@file:Import("SlackMessage.main.kts")

import java.util.concurrent.TimeUnit

val TokenArgumentKey = "--token"
val WebHookKey = "--webhook"

if(!args.contains(TokenArgumentKey) || !args.contains(WebHookKey)){
    val usage = """
        Use this tool to create a release branch
        Usage: ./Frozono.main.kts --token <token> --webhook <webhook_url>
        
        Options:
         --token         Token to access the repository
         --webhook       Slack webhook url for notification purposes
    """

    throw IllegalStateException(usage)
}

val RepositoryUriAppendix = "github.com/oscarg798/Remembrall"
val token = getArgument("--token")
val WebHookUrl = getArgument("--webhook")
val Repository = "https://$token@$RepositoryUriAppendix"
val CurrentVersion = Version.createFromProperties()
val SourceBranch = "master"

Hermes.display("current version: $CurrentVersion")

val branchName = "release/v$CurrentVersion"

runCatching {
    createBranch(branchName= branchName, source = SourceBranch, repository= Repository)
}.onFailure { error ->
    Hermes.display("There was an error: $error")
    Hermes.notify(
        slackMessage = getErrorSlackMessage(error),
        webHookUrl = WebHookUrl
    )
}.onSuccess {
    Hermes.notify(
        slackMessage = getSuccessSlackMessage(),
        webHookUrl = WebHookUrl
    )
}

fun getSuccessSlackMessage() = SlackMessage.Message(
    blocks = listOf<SlackMessage.Block>(
        getMessageTitle(),
        SlackMessage.Block.Divier,
        SlackMessage.Block.TextFieldSection(
            listOf(
                SlackMessage.TextField.Markdown("*Release* branch created successfully"),
                SlackMessage.TextField.Markdown("Version *v$CurrentVersion*"),
                SlackMessage.TextField.Markdown(":fireworks: :) ")
            )
        ),
        SlackMessage.Block.Text(SlackMessage.TextField.Markdown("@channel")),
        SlackMessage.Block.Divier,
        SlackMessage.Block.Actions(
            listOf(SlackMessage.Button(
                style = SlackMessage.ButtonStyle.Primary,
                text = SlackMessage.TextField.PlainText("Check it out"),
                url = "https://$RepositoryUriAppendix/tree/$branchName"
            ))
        )
    )
)

fun getErrorSlackMessage(error : Throwable) = SlackMessage.Message(
    blocks = listOf<SlackMessage.Block>(
        getMessageTitle(),
        SlackMessage.Block.Divier,
        SlackMessage.Block.TextFieldSection(
            listOf(
                SlackMessage.TextField.Markdown("Error creating **Release** branch for version *v$CurrentVersion* :warning: :boom:"),
                SlackMessage.TextField.Markdown("*Error Output* $error")
            )
        ),
        SlackMessage.Block.Text(SlackMessage.TextField.Markdown("@channel")),
        SlackMessage.Block.Divier,
        SlackMessage.Block.Actions(
           listOf( SlackMessage.Button(
               style = SlackMessage.ButtonStyle.Danger,
               text = SlackMessage.TextField.PlainText("Review it "),
               url =  "https://$RepositoryUriAppendix/actions"
           ))
        )
    )
)

fun getMessageTitle() = SlackMessage.Block.Text(SlackMessage.TextField.Markdown("*Frozono* code freeze:"))

fun getArgument(argumentKey: String): String  {
    return  if (args.contains(argumentKey)){
        args[1 + args.indexOf(argumentKey)]
    } else {
        throw IllegalArgumentException("Argument $argumentKey not found")
    }
}

