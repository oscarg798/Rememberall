import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Hades is the king of the Infierno, he controls
 * the things that go down there, so in other words,
 * Hades is an utility to run bash commands.
 */
object Hades {

    fun exec(command: Command) {
        val process = ProcessBuilder(command.getArguments())
            .directory(File(command.path))
            .start()
            .also { it.waitFor(command.timeOut, TimeUnit.MINUTES) }

        if (process.exitValue() != ErrorValue) {
            throw IllegalStateException(process.errorStream.bufferedReader().readText())
        }

        process.inputStream.bufferedReader()
            .forEachLine { command.commandOutputListener.onOutput(it) }
    }

    interface CommandOutputListener {

        fun onOutput(output: String)
    }

    data class Command(
        val command: String,
        val commandOutputListener: CommandOutputListener,
        val path: String,
        val timeOut: Long = DefaultTimeOutMinutes,

        ) {

        fun getArguments(): List<String> = command.split(CommandSeparatorToken)
    }

    private const val CommandSeparatorToken = " "
    private const val ErrorValue = 0
    private const val DefaultTimeOutMinutes = 1L
}