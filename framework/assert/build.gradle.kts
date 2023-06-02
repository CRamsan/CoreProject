plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.assertlib"
}

kotlin {
    js {
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":framework:halt"))
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:logging"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}