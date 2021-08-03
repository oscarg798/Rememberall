dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "remembrall"
include(":app")
include(":ui-common")
include(":tasklist")
include(":common")
include(":common-auth")
include(":taskdetail")
include(":common-gettask")
