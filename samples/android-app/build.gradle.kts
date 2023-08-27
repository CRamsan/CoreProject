plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    implementation("androidx.compose.ui:ui-tooling-preview:_")
    implementation("androidx.compose.material3:material3:_")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:_")

    debugImplementation("androidx.compose.ui:ui-tooling:_")
    debugImplementation("androidx.compose.ui:ui-test-manifest:_")
}
