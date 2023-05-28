import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.library")
    kotlin("kapt")
}

apply (from = "$rootDir/gradle/android-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.ui"

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    kapt {
        correctErrorTypes = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
}

dependencies {
    implementation (project(":auraxiscontrolcenter:core-models"))
    implementation (project(":framework:assert"))
    implementation (project(":framework:interfacelib"))
    implementation (Google.accompanist.swipeRefresh)
    implementation (Google.android.material)
    implementation (AndroidX.appCompat)
    implementation (AndroidX.activity.ktx)
    implementation (AndroidX.fragment.ktx)
    implementation (COIL)
    implementation (COIL.compose)
    implementation (KotlinX.collections.immutable)
    implementation (AndroidX.constraintLayout)
    implementation (AndroidX.compose.ui)
    implementation (AndroidX.compose.ui.tooling)
    implementation (AndroidX.compose.foundation)
    implementation (AndroidX.compose.material)
    implementation (AndroidX.compose.material.icons.core)
    implementation (AndroidX.compose.material.icons.extended)
    implementation (AndroidX.compose.runtime.liveData)
    implementation (KotlinX.datetime)
    implementation ("org.ocpsoft.prettytime:prettytime:_")
}