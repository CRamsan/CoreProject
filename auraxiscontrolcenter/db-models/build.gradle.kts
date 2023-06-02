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
    js {
        nodejs()
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
        val commonMain by getting {
            dependencies {
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
