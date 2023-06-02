plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
    kotlin("jvm") apply false
    kotlin("js") apply false
    kotlin("multiplatform") apply false
    kotlin("kapt") apply false
    id("com.google.dagger.hilt.android") apply false
    id("org.jetbrains.compose") apply false
    kotlin("plugin.serialization") apply false
    id("androidx.navigation.safeargs.kotlin") apply false
    id("com.squareup.sqldelight") apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.0" apply false
}

tasks.register("buildAll") {
    group = "build"
    description = "Builds all target"

    dependsOn("framework:assert:build")
    dependsOn("framework:crashhandler:build")
    dependsOn("framework:core:build")
    dependsOn("framework:core-compose:build")
    dependsOn("framework:halt:build")
    dependsOn("framework:interfacelib:build")
    dependsOn("framework:interfacelib-test:build")
    dependsOn("framework:logging:build")
    dependsOn("framework:metrics:build")
    dependsOn("framework:userevents:build")
    dependsOn("framework:preferences:build")
    dependsOn("framework:remoteconfig:build")
    dependsOn("framework:thread:build")
    dependsOn("framework:test:build")
    dependsOn("framework:utils:build")

    dependsOn("auraxiscontrolcenter:app:build")
    dependsOn("auraxiscontrolcenter:appcore:build")
    dependsOn("auraxiscontrolcenter:cdk:build")
    dependsOn("auraxiscontrolcenter:core-models:build")
    dependsOn("auraxiscontrolcenter:db-models:build")
    dependsOn("auraxiscontrolcenter:deployable-models:build")
    dependsOn("auraxiscontrolcenter:network-models:build")
    dependsOn("auraxiscontrolcenter:streaming-client:build")
    dependsOn("auraxiscontrolcenter:desktop:build")
    dependsOn("auraxiscontrolcenter:ui:build")

    dependsOn("stranded:lib:build")
    dependsOn("stranded:gdx:core:build")
    dependsOn("stranded:cardmanager:build")
    dependsOn("stranded:testgui:build")
    dependsOn("stranded:server:build")
    dependsOn("stranded:server:demogame:build")
    dependsOn("stranded:web:build")

    dependsOn("samples:android-app:build")
    dependsOn("samples:android-lib:build")
    dependsOn("samples:jbcompose-mpp-lib:build")
    dependsOn("samples:jbcompose-desktop-app:build")
    dependsOn("samples:jbcompose-android-app:build")
    dependsOn("samples:mpp-lib:build")
    dependsOn("samples:jvm-lib:build")
    dependsOn("samples:nodejs-app:build")

    dependsOn("cdk-repo:build")
}
