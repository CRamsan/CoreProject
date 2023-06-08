pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.51.0"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != de.fayard.refreshVersions.core.StabilityLevel.Stable
    }
}

include("framework:assert")
include("framework:crashhandler")
include("framework:core")
include("framework:core-compose")
include("framework:halt")
include("framework:interfacelib")
include("framework:interfacelib-test")
include("framework:logging")
include("framework:metrics")
include("framework:userevents")
include("framework:preferences")
include("framework:remoteconfig")
include("framework:thread")
include("framework:test")
include("framework:utils")

include("auraxiscontrolcenter:app")
include("auraxiscontrolcenter:appcore")
include("auraxiscontrolcenter:app-frontend")
include("auraxiscontrolcenter:cdk")
include("auraxiscontrolcenter:core-models")
include("auraxiscontrolcenter:db-models")
include("auraxiscontrolcenter:deployable-models")
include("auraxiscontrolcenter:network-models")
include("auraxiscontrolcenter:streaming-client")
include("auraxiscontrolcenter:desktop")
include("auraxiscontrolcenter:ui")

include("stranded:lib")
include("stranded:gdx:core")
include("stranded:cardmanager")
include("stranded:testgui")
include("stranded:server")
include("stranded:server:demogame")
include("stranded:web")

include("samples:android-app")
include("samples:android-lib")
include("samples:cdk-app")
include("samples:jbcompose-mpp-lib")
include("samples:jbcompose-desktop-app")
include("samples:jbcompose-android-app")
include("samples:mpp-lib")
include("samples:jvm-lib")
include("samples:nodejs-app")

include("tpsd")

include("cdk-repo")
