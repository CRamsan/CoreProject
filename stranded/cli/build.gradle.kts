plugins {
    kotlin("plugin.serialization")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(project(":stranded:server"))
    implementation(project(":stranded:lib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
}