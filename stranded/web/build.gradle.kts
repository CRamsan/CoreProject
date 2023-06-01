plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.stranded.web"
}

kotlin {
    js() {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation(project(":stranded:lib"))
                implementation(KotlinX.serialization.json)
            }
        }
        val jsTest by getting {
            dependencies {
            }
        }
    }
}
