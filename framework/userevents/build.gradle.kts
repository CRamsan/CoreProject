plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.userevents"
}

dependencies {
    implementation("com.microsoft.appcenter:appcenter-analytics:_")
    implementation("com.microsoft.appcenter:appcenter-crashes:_")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}