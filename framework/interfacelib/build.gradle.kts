plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.interfacelib"
}

kotlin {
    js() {
        nodejs()
        browser()
    }
}