plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.test"
}

kotlin {
    js() {
        nodejs()
        browser()
    }
}

dependencies {
    implementation(AndroidX.test.core)
    implementation(AndroidX.test.ext.junit)
    implementation(AndroidX.test.ext.junit.ktx)
    implementation(AndroidX.archCore.common)
    implementation(AndroidX.archCore.runtime)
    implementation(AndroidX.archCore.testing)
    implementation(KotlinX.coroutines.test)
    implementation(Testing.junit4)
    implementation(Kotlin.test)
    implementation(Kotlin.test.junit)
    implementation(Testing.mockK)
    implementation(Testing.mockK.android)

    implementation(AndroidX.lifecycle.liveDataKtx)

    testImplementation(AndroidX.lifecycle.viewModelKtx)
}

kotlin {

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))

                implementation(KotlinX.coroutines.test)
                implementation(Kotlin.test.common)
                implementation(Kotlin.test.annotationsCommon)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(KotlinX.coroutines.test)
                implementation(Testing.junit.jupiter.api)
                implementation(Kotlin.test.junit5)
                implementation(Testing.mockK)
            }
        }
        val jvmTest by getting {
        }
        val androidMain by getting {
        }
        val androidUnitTest by getting {
        }
        val jsMain by getting {
            dependencies {
                implementation(Kotlin.test.js)
                implementation(KotlinX.coroutines.test)
            }
        }
    }
}
