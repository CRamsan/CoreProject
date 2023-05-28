plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.network.model"
}

dependencies {
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:assert"))
                implementation(project(":framework:crashhandler"))
                implementation(project(":framework:core"))
                implementation(project(":framework:halt"))
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:interfacelib-test"))
                implementation(project(":framework:logging"))
                implementation(project(":framework:metrics"))
                implementation(project(":framework:userevents"))
                implementation(project(":framework:preferences"))
                implementation(project(":framework:remoteconfig"))
                implementation(project(":framework:thread"))
                implementation(project(":framework:test"))
                implementation(project(":framework:utils"))
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