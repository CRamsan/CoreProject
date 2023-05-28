plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.network.ws"
}

/**
 * Configure multiplatform project
 */
kotlin {
    js(IR) {
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
                implementation(project(":framework:interfacelib"))

                implementation(KotlinX.serialization.json)
                implementation("io.ktor:ktor-client-websockets:_")
                implementation(Ktor.client.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                // Serer dependencies
                implementation(Ktor.client.cio)
                implementation(Ktor.server.core)
                implementation(Ktor.server.netty)
                implementation(Ktor.server.websockets)
            }
        }
        val jvmTest by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
                // Client dependencies
                implementation("io.ktor:ktor-client-js:_")
            }
        }
    }
}
