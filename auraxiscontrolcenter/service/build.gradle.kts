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

    implementation("io.insert-koin:koin-core:3.4.2")
    implementation(KotlinX.datetime)
    implementation(KotlinX.serialization.json)
    implementation(Square.sqlDelight.drivers.jdbcSqlite)

    implementation("org.slf4j:slf4j-api:_")
    implementation("ch.qos.logback:logback-core:_")
    implementation("ch.qos.logback:logback-classic:_")

    implementation(Ktor.server.core)
    implementation(Ktor.server.netty)
    implementation(Ktor.server.websockets)
}