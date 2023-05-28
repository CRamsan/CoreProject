plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    kotlin("kapt")
}

apply(from = "$rootDir/gradle/kotlin-mpp-lib.gradle")

android {
    namespace = "com.cramsan.ps2link.appcore"
}

dependencies {
    "kapt"(Google.dagger.hilt.android.compiler)

    implementation(Google.dagger.hilt.android)

    implementation(Square.sqlDelight.drivers.android)
    implementation(Ktor.client.android)
    implementation("org.twitter4j:twitter4j-core:_")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":framework:assert"))
                implementation(project(":framework:crashhandler"))
                implementation(project(":framework:core"))
                implementation(project(":framework:halt"))
                implementation(project(":framework:interfacelib"))
                implementation(project(":framework:interfacelib-test"))
                implementation(project(":framework:logging"))
                implementation(project(":framework:metrics"))
                implementation(project(":framework:userevents"))
                implementation(project(":framework:preferences"))
                implementation(project(":framework:remoteconfig"))
                implementation(project(":framework:thread"))
                implementation(project(":framework:test"))
                implementation(project(":framework:utils"))
                implementation(project(":auraxiscontrolcenter:core-models"))
                implementation(project(":auraxiscontrolcenter:db-models"))
                implementation(project(":auraxiscontrolcenter:deployable-models"))
                implementation(project(":auraxiscontrolcenter:network-models"))
                implementation(Ktor.client.core)
                implementation(Ktor.client.contentNegotiation)
                implementation(Ktor.plugins.serialization.kotlinx.json)
                implementation(KotlinX.datetime)
                implementation(KotlinX.serialization.json)
                implementation(Square.sqlDelight.extensions.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":framework:test"))
            }
        }
    }
}