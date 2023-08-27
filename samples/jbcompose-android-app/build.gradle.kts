plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("org.jetbrains.compose")
}

apply(from = "$rootDir/gradle/android-app.gradle")

android {
    namespace = "com.cramsan.framework.sample.jbcompose.mpplib"

    defaultConfig {
        applicationId = "com.cramsan.framework.sample.jbcompose.mpplib"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":samples:jbcompose-mpp-lib"))

    implementation(AndroidX.activity.compose)

    implementation("androidx.compose.ui:ui-tooling-preview:_")
    debugImplementation("androidx.compose.ui:ui-tooling:_")
    implementation("androidx.compose.foundation:foundation:_")

    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
}