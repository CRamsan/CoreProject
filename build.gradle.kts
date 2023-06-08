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
    id("io.gitlab.arturbosch.detekt") apply false
}

tasks.register("releaseAll") {
    group = "build"
    description = "Builds all target"

    dependsOn("framework:assert:release")
    dependsOn("framework:crashhandler:release")
    dependsOn("framework:core:release")
    dependsOn("framework:core-compose:release")
    dependsOn("framework:halt:release")
    dependsOn("framework:interfacelib:release")
    dependsOn("framework:interfacelib-test:release")
    dependsOn("framework:logging:release")
    dependsOn("framework:metrics:release")
    dependsOn("framework:userevents:release")
    dependsOn("framework:preferences:release")
    dependsOn("framework:remoteconfig:release")
    dependsOn("framework:thread:release")
    dependsOn("framework:test:release")
    dependsOn("framework:utils:release")

    dependsOn("auraxiscontrolcenter:app:release")
    dependsOn("auraxiscontrolcenter:appcore:release")
    dependsOn("auraxiscontrolcenter:cdk:release")
    dependsOn("auraxiscontrolcenter:core-models:release")
    dependsOn("auraxiscontrolcenter:db-models:release")
    dependsOn("auraxiscontrolcenter:deployable-models:release")
    dependsOn("auraxiscontrolcenter:network-models:release")
    dependsOn("auraxiscontrolcenter:streaming-client:release")
    dependsOn("auraxiscontrolcenter:desktop:release")
    dependsOn("auraxiscontrolcenter:ui:release")

    dependsOn("samples:android-app:release")
    dependsOn("samples:android-lib:release")
    dependsOn("samples:jbcompose-mpp-lib:release")
    dependsOn("samples:jbcompose-desktop-app:release")
    dependsOn("samples:jbcompose-android-app:release")
    dependsOn("samples:mpp-lib:release")
    dependsOn("samples:jvm-lib:release")
    dependsOn("samples:nodejs-app:release")

    dependsOn("cdk-repo:release")

    dependsOn("tpsd:release")
}
