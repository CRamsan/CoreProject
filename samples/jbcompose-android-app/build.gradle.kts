plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":samples:jbcompose-mpp-lib"))

                implementation(AndroidX.activity.compose)
                implementation(AndroidX.appCompat)
                implementation(AndroidX.core.ktx)
            }
        }
    }
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.cramsan.minesweepers.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "com.cramsan.minesweepers.android"
}
