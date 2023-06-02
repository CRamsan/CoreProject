plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.preferences"
}

kotlin {
    js(){
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
                implementation(project(":framework:interfacelib-test"))
            }
        }
    }
}