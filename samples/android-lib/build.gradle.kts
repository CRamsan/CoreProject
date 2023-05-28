plugins {
    id("com.android.library")
}

apply(from = "$rootDir/gradle/android-lib.gradle")

android {
    namespace = "com.cramsan.framework.sample.android.lib"
}
