plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.stranded.server"
}

/**
 * Configure multiplatform project
 */
kotlin {
    js() {
        browser {
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(KotlinX.serialization.json)
                implementation("io.ktor:ktor-client-websockets:_")
                implementation(Ktor.client.core)
            }
        }
        val commonTest by getting {
            dependencies {
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