plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.remoteconfig"
}

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))
                implementation(Ktor.client.core)
                implementation(Ktor.client.contentNegotiation)
                implementation(KotlinX.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}