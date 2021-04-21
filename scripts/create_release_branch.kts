import java.io.File
import java.util.concurrent.TimeUnit

val projectDir = System.getProperty("user.dir")
val versionFile = File("$projectDir/current_version")
val lines = mutableListOf<String>()
versionFile.forEachLine{
    lines.add(it)
}

val version = lines.first()
var output = ""

println("##################################################")
println("current Version: $version")

println("##################################################")
println("checking out master")

output += File(projectDir).exec("git checkout master")

val branchName = "release/v$version"

println("##################################################")
println("creating release branch: $branchName")

output += File(projectDir).exec("git checkout -b $branchName")

println("##################################################")
println("branch created")

println("##################################################")
println("pushing branch")

output += File(projectDir).exec("git push -u ")

println("##################################################")
println("branch pushed")
println(output)

infix fun File.exec(command: String): String {
    val arguments = command.split(' ').toTypedArray()
    return execute(*arguments)
}

fun File.execute(vararg arguments: String): String {
    val process = ProcessBuilder(*arguments)
        .directory(this)
        .start()
        .also { it.waitFor(1, TimeUnit.MINUTES) }

    if (process.exitValue() != 0) {
        throw Exception(process.errorStream.bufferedReader().readText())
    }
    return process.inputStream.bufferedReader().readText()
}
