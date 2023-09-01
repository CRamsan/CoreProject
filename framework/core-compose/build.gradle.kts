import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
}

apply(from = "$rootDir/gradle/android-lib.gradle")

android {
    namespace = "com.cramsan.framework.core.compose"

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }

    dependencies {
        implementation("com.google.dagger:hilt-android:_")

        implementation(project(":framework:interfacelib"))
        implementation(project(":framework:core"))

        implementation("com.google.android.material:material:_")
        implementation("androidx.appcompat:appcompat:_")
        implementation("androidx.activity:activity-compose:_")
        implementation("androidx.fragment:fragment-ktx:_")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:_")
        implementation("androidx.compose.ui:ui:_")
        debugImplementation(AndroidX.compose.ui.tooling)
        implementation("androidx.compose.ui:ui-tooling-preview:_")
        implementation(AndroidX.compose.foundation)
    }
}