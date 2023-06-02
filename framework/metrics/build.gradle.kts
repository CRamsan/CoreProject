plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.metrics"
}

kotlin {
    js {
        nodejs()
    }

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
        val jvmMain by getting {
            dependencies {
                implementation("software.amazon.awssdk:auth:_")
                implementation("software.amazon.awssdk:cloudwatch:_")
            }
        }
    }
}

dependencies {
    implementation("com.amazonaws:aws-android-sdk-cloudwatch:_")
}