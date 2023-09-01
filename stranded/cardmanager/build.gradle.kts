import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(project(":framework:test"))
    implementation(project(":stranded:lib"))
    implementation(project(":stranded:server"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:_")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
}

compose.desktop {
    application {
        mainClass = "com.cramsan.stranded.cardmanager.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CardManager"
            packageVersion = "1.0.0"
        }
    }
}
