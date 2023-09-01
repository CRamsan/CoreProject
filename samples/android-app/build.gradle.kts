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

    implementation("androidx.activity:activity-compose:_")
    implementation("androidx.appcompat:appcompat:_")
    implementation("com.google.android.material:material:_")
    implementation(AndroidX.navigation.fragmentKtx)
    implementation("androidx.navigation:navigation-ui-ktx:_")
    implementation("androidx.compose.ui:ui:_")
    implementation("androidx.compose.ui:ui-graphics:_")
    implementation("androidx.compose.ui:ui-tooling-preview:_")
    implementation("androidx.compose.material3:material3:_")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:_")

    debugImplementation("androidx.compose.ui:ui-tooling:_")
    debugImplementation("androidx.compose.ui:ui-test-manifest:_")
}
