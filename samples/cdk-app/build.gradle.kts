val mainClassTarget by extra("com.cesarandres.ps2link.aws.PS2LinkApp")

plugins {
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-cdk.gradle")
