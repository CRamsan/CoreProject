import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":samples:jbcompose-mpp-lib"))
}

compose.desktop {
    application {
        mainClass = "com.cramsan.minesweepers.jvm.WindowKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinMultiplatformComposeDesktopApplication"
            packageVersion = "1.0.0"
        }
    }
}
