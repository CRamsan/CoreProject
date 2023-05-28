plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

sqldelight {
    database("PS2LinkDB") {
        packageName = "com.cramsan.ps2link.db.models"
    }
}

android {
    namespace = "com.cramsan.ps2link.db.model"
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
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
                implementation(Square.sqlDelight.extensions.coroutines)
                implementation(KotlinX.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}
