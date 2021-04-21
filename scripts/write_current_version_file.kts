import java.io.File
import java.util.Properties

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val buildNumber: Int
) {

    override fun toString(): String {
        return "$major.$minor.${patch}_$buildNumber"
    }
}

val PropertyToken = "="
val SplittedPropertiesRequiredSize = 2
val Major = "major"
val Minor = "minor"
val Patch = "patch"
val BuildNumber = "buildNumber"
val Properties = Properties()

val projectDir = System.getProperty("user.dir")
val propertiesFile = File("$projectDir/gradle.properties")
val properties = Properties.load(propertiesFile.inputStream())
val currentVersion = Version(
    major = Properties.getProperty(Major).toInt(),
    minor = Properties.getProperty(Minor).toInt(),
    patch = Properties.getProperty(Patch).toInt(),
    buildNumber = Properties.getProperty(BuildNumber).toInt()
)
println("Current version: $currentVersion")

println("writing version file")
val path = "$projectDir/current_version"
File(path).printWriter().use { out -> out.println(currentVersion) }
println("version wrote")


