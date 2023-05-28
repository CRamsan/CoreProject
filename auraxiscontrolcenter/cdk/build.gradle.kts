val mainClassTarget by extra("com.cesarandres.ps2link.aws.PS2LinkApp")

plugins {
    kotlin("plugin.serialization")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-cdk.gradle")

dependencies {
    implementation(KotlinX.serialization.json)
    implementation(project(":auraxiscontrolcenter:deployable-models"))
    implementation(project(":cdk-repo"))
    implementation(project(":framework:interfacelib"))
}
