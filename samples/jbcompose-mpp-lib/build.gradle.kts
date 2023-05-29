@file:Suppress("OPT_IN_IS_NOT_ENABLED")

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0-SNAPSHOT"

apply(from = "$rootDir/gradle/kotlin-mpp-compose-lib.gradle")

android {
    namespace = "com.cramsan.framework.sample.mpp_lib.compose"
}

kotlin {
    cocoapods {
        summary = "Some description for the JBCcomposeMPPLib Module"
        homepage = "Link to the jbcompose-ios-app Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../jbcompose-ios-app/Podfile")
        framework {
            baseName = "JBComposeMPPLib"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }
}