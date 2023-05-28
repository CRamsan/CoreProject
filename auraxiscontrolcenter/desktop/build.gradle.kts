import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "com.cramsan.ps2link.network.ws.testgui.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe)
            packageName = "PS2Link"
            packageVersion = "1.0.5"
            modules("java.instrument", "java.management", "java.prefs", "jdk.unsupported")
            windows {
                iconFile.set(project.file("icon_large.ico"))
                perUserInstall = true
                menuGroup = "CRamsan"
                upgradeUuid = "81f4aaa2-6a1a-11ed-a1eb-0242ac120002"
            }
        }
    }
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("com.github.kwhat:jnativehook:_")

    implementation(project(":framework:assert"))
    implementation(project(":framework:core"))
    implementation(project(":framework:halt"))
    implementation(project(":framework:interfacelib"))
    implementation(project(":framework:logging"))
    implementation(project(":framework:preferences"))
    implementation(project(":framework:thread"))
    implementation(project(":auraxiscontrolcenter:streaming-client"))
    implementation(project(":auraxiscontrolcenter:core-models"))
    implementation(project(":auraxiscontrolcenter:appcore"))
    implementation(project(":framework:interfacelib"))
    implementation(KotlinX.datetime)
    implementation(KotlinX.collections.immutable)
    implementation(KotlinX.serialization.json)
    implementation("io.ktor:ktor-client-websockets:_")
    implementation(Ktor.client.core)
    implementation("org.ocpsoft.prettytime:prettytime:_")
    implementation(Square.sqlDelight.drivers.jdbcSqlite)
}