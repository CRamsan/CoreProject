import de.fayard.refreshVersions.core.versionFor

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
}

version = "1.0"

apply(from = "$rootDir/gradle/kotlin-mpp-compose-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.ui"
}

dependencies {
    implementation (COIL.base)
    implementation (COIL.compose)
    implementation (Google.android.material)
    implementation (AndroidX.compose.material.icons.core)
    implementation (AndroidX.compose.material.icons.extended)
}

kotlin {
    cocoapods {
        summary = "PS2 Link UI Library"
        homepage = "http://"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "PS2LinkUI"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":auraxiscontrolcenter:core-models"))
                implementation(KotlinX.collections.immutable)
                implementation(KotlinX.datetime)
            }
        }
    }
}