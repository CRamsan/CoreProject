plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("org.jetbrains.compose")
}

apply(from = "$rootDir/gradle/android-app.gradle")

android {
    namespace = "com.cramsan.framework.sample.android"

    defaultConfig {
        applicationId = "com.cramsan.framework.sample.android"
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(project(":samples:jbcompose-mpp-lib"))

    implementation(project(":framework:assert"))
    implementation(project(":framework:crashhandler"))
    implementation(project(":framework:core"))
    implementation(project(":framework:halt"))
    implementation(project(":framework:interfacelib"))
    implementation(project(":framework:interfacelib-test"))
    implementation(project(":framework:logging"))
    implementation(project(":framework:metrics"))
    implementation(project(":framework:userevents"))
    implementation(project(":framework:preferences"))
    implementation(project(":framework:remoteconfig"))
    implementation(project(":framework:thread"))
    implementation(project(":framework:test"))
    implementation(project(":framework:utils"))

    implementation(AndroidX.activity.compose)

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.foundation)

    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}