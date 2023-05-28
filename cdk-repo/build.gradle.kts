plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}
apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(KotlinX.serialization.json)
    implementation("software.amazon.awscdk:aws-cdk-lib:_")
}