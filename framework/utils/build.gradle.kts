plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.utils"

    dependencies {
        testImplementation(project(":framework:test"))
    }
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
