import java.io.File
import java.nio.file.Paths
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

    companion object {
        private const val PropertyToken = "="
        private const val SplittedPropertiesRequiredSize = 2
        private const val Major = "major"
        private const val Minor = "minor"
        private const val Patch = "patch"
        private const val BuildNumber = "buildNumber"
        private const val UserDir = "user.dir"
        private const val PropertiesFileRelativePath = "../gradle.properties"

        fun createFromProperties(): Version {
            val propertiesFilePath = getPropertiesFilePath()

            val propertiesFile = File("$propertiesFilePath")

            val properties = Properties()
            properties.load(propertiesFile.inputStream())

            return Version(
                major = properties.getProperty(Major).toInt(),
                minor = properties.getProperty(Minor).toInt(),
                patch = properties.getProperty(Patch).toInt(),
                buildNumber = properties.getProperty(BuildNumber).toInt()
            )
        }

        private fun getPropertiesFilePath() = Paths.get("")
            .toAbsolutePath()
            .resolve("$PropertiesFileRelativePath")
    }
}