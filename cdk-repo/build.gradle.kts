plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}
apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
    implementation("software.amazon.awscdk:aws-cdk-lib:_")
}