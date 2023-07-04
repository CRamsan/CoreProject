val mainClassTarget by extra("com.cramsan.ps2link.service.PS2LinkServiceKt")

plugins {
    kotlin("plugin.serialization")
    kotlin("jvm")
    id("io.ktor.plugin")
}

apply(from = "$rootDir/gradle/kotlin-jvm-application.gradle")

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":framework:assert"))
    implementation(project(":framework:core"))
    implementation(project(":framework:interfacelib"))
    implementation(project(":framework:logging"))
    implementation(project(":framework:preferences"))
    implementation(project(":framework:thread"))
    implementation(project(":framework:interfacelib"))

    implementation(project(":auraxiscontrolcenter:streaming-client"))
    implementation(project(":auraxiscontrolcenter:network-models"))
    implementation("io.insert-koin:koin-core:3.4.2")
    implementation(KotlinX.datetime)
    implementation(KotlinX.serialization.json)
    implementation(Square.sqlDelight.drivers.jdbcSqlite)
    implementation("org.litote.kmongo:kmongo:_")
    implementation("org.litote.kmongo:kmongo-coroutine:_")

    implementation("org.slf4j:slf4j-api:_")
    implementation("ch.qos.logback:logback-core:_")
    implementation("ch.qos.logback:logback-classic:_")

    implementation(Ktor.server.core)
    implementation(Ktor.server.netty)
    implementation(Ktor.server.websockets)
    implementation(Ktor.server.contentNegotiation)

    implementation(Ktor.client.core)
    implementation(Ktor.client.java)
    implementation(Ktor.client.contentNegotiation)
    implementation(Ktor.plugins.serialization.kotlinx.json)
    implementation(project(mapOf("path" to ":auraxiscontrolcenter:app-frontend")))
}