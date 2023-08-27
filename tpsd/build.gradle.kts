plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

apply(from = "$rootDir/gradle/kotlin-jvm-lib.gradle")

dependencies {
    implementation(project(":framework:interfacelib"))
    implementation(project(":framework:logging"))

    implementation("com.github.twitch4j:twitch4j:_")
    implementation(KotlinX.coroutines.swing)
    implementation(Ktor.client.core)
    implementation(Ktor.client.java)
    implementation(KotlinX.serialization.json)
    implementation(Ktor.client.contentNegotiation)
    implementation(Ktor.plugins.serialization.kotlinx.json)
    implementation(KotlinX.cli)

    implementation(compose.desktop.currentOs)

    testImplementation(kotlin("test"))
    testImplementation(KotlinX.coroutines.test)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}