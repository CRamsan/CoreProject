plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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

    implementation("androidx.activity:activity-compose:_")

    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.foundation)

    implementation("androidx.appcompat:appcompat:_")
    implementation("androidx.core:core-ktx:_")
    implementation(AndroidX.lifecycle.runtime.ktx)

    implementation(platform(AndroidX.compose.bom))
    implementation("androidx.compose.ui:ui-graphics:_")
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.navigation.compose)
}