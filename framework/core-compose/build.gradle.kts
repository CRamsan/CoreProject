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
        implementation(Google.dagger.hilt.android)

        implementation(project(":framework:interfacelib"))
        implementation(project(":framework:core"))

        implementation(Google.android.material)
        implementation(AndroidX.appCompat)
        implementation(AndroidX.activity.compose)
        implementation(AndroidX.fragment.ktx)
        implementation(AndroidX.lifecycle.viewModelKtx)
        implementation(AndroidX.compose.ui)
        debugImplementation(AndroidX.compose.ui.tooling)
        implementation("androidx.compose.ui:ui-tooling-preview:_")
        implementation(AndroidX.compose.foundation)
    }
}