plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.framework.test"
}

kotlin {
    js {
        nodejs()
        browser()
    }
}

dependencies {
    implementation("androidx.test:core:_")
    implementation("androidx.test.ext:junit:_")
    implementation("androidx.test.ext:junit-ktx:_")
    implementation("androidx.arch.core:core-common:_")
    implementation("androidx.arch.core:core-runtime:_")
    implementation("androidx.arch.core:core-testing:_")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
    implementation("junit:junit:_")
    implementation("org.jetbrains.kotlin:kotlin-test:_")
    implementation("org.jetbrains.kotlin:kotlin-test-junit:_")
    implementation("io.mockk:mockk:_")
    implementation("io.mockk:mockk-android:_")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:_")

    testImplementation("androidx.lifecycle:lifecycle-viewmodel-ktx:_")
}

kotlin {

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:interfacelib"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
                implementation("org.jetbrains.kotlin:kotlin-test-common:_")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:_")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
                implementation("org.junit.jupiter:junit-jupiter-api:_")
                implementation("org.jetbrains.kotlin:kotlin-test-junit5:_")
                implementation("io.mockk:mockk:_")
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
                implementation("org.jetbrains.kotlin:kotlin-test-js:_")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:_")
            }
        }
    }
}
