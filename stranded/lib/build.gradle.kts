plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.stranded.lib"
}

/**
 * Configure multiplatform project
 */
kotlin {
    js {
        browser { 
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":stranded:server"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":stranded:server"))
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }
    }
}