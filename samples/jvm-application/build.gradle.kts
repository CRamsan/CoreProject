plugins {
    kotlin("jvm")
}

val mainClassTarget by extra("com.cramsan.framework.sample.jvm.application.ApplicationKt")

apply(from = "$rootDir/gradle/kotlin-jvm-application.gradle")

dependencies {
    implementation(project(":samples:jvm-lib"))
    implementation(project(":samples:mpp-lib"))
}