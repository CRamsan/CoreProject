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
    implementation("com.google.dagger:hilt-android:_")

    implementation(project(":framework:interfacelib"))

    implementation("com.google.android.material:material:_")
    implementation("androidx.appcompat:appcompat:_")
    implementation("androidx.fragment:fragment-ktx:_")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:_")
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
