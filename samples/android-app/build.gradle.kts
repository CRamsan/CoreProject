plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

apply(from = "$rootDir/gradle/android-app.gradle")

android {
    namespace = "com.cramsan.framework.sample.android.app"

    defaultConfig {
        applicationId = "com.cramsan.framework.sample.android.app"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":samples:android-lib"))
    implementation(project(":samples:mpp-lib"))
    implementation(project(":samples:jvm-lib"))

    implementation(AndroidX.activity.compose)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.graphics)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.compose.material3)

    androidTestImplementation(AndroidX.compose.ui.testJunit4)

    debugImplementation(AndroidX.compose.ui.tooling)
    debugImplementation(AndroidX.compose.ui.testManifest)
}
