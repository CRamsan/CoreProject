plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.halt"
}

dependencies {
    implementation("com.android.support:support-compat:_")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:logging"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}