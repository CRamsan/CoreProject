import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    kotlin("jvm")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(project(":stranded:lib"))
    implementation(project(":stranded:server"))
    implementation(KotlinX.coroutines.swing)
    implementation(KotlinX.serialization.json)

    testImplementation(project(":framework:test"))
}

compose.desktop {
    application {
        mainClass = "com.cramsan.stranded.testgui.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "TestGui"
            packageVersion = "1.0.0"
        }
    }
}