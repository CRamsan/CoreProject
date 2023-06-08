plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("kapt")
}

apply(from = "$rootDir/gradle/kotlin-mpp-compose-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.appfrontend"
}

dependencies {
    "kapt"(Google.dagger.hilt.android.compiler)

    implementation(Google.dagger.hilt.android)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:core"))
                implementation(project(":auraxiscontrolcenter:appcore"))
                implementation(project(":auraxiscontrolcenter:core-models"))
                implementation(project(":auraxiscontrolcenter:ui"))

                implementation(KotlinX.collections.immutable)
                implementation(KotlinX.datetime)
            }
        }
    }
}