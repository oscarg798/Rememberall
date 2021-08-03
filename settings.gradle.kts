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
include(":profile")
include(":common_calendar")
include(":common-schedule")
include(":splash")
include(":edittask")
include(":common-addedit")
include(":addtask")
