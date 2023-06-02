plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("kapt")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.core"
}

dependencies {
    //kapt(Google.dagger.hilt.android.compiler)
    //kapt(Google.dagger.hilt.android.gradlePlugin)

    implementation(Google.dagger.hilt.android)

    implementation(project(":framework:interfacelib"))

    implementation(Google.android.material)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.lifecycle.viewModelKtx)
}

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:assert"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}
