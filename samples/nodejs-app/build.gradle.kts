plugins {
    kotlin("js")
}

apply(from = "$rootDir/gradle/kotlin-js-lib.gradle")

dependencies {
    implementation(project(":samples:mpp-lib"))
}

kotlin {
    js {
        nodejs { }
        binaries.executable()
    }
}