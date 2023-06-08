import de.fayard.refreshVersions.core.versionFor

plugins {
    id("com.android.application")
}
apply(from = "$rootDir/gradle/android-app.gradle")

android {
    namespace = "com.cesarandres.ps2link"

    defaultConfig {
        applicationId = "com.cesarandres.ps2link"
        versionName = "5.7.3"

        val consumerSecret = findProperty("ps2linkConsumerSecret") ?: ""
        val consumerKey = findProperty("ps2linkConsumerKey") ?: ""
        val accessToken = findProperty("ps2linkAccessToken") ?: ""
        val accessTokenSecret = findProperty("ps2linkAccessTokenSecret") ?: ""
        val appCenterIdPreProd = findProperty("ps2linkAppCenterIdPreProd") ?: ""
        val appCenterIdProd = findProperty("ps2linkAppCenterIdProd") ?: ""
        val serviceIdPreProd = findProperty("ps2linkServiceIdPreProd") ?: ""
        val serviceIdProd = findProperty("ps2linkServiceIdProd") ?: ""
        val awsAccessKeyPreProd = findProperty("ps2linkAwsAccessKeyPreProd") ?: ""
        val awsSecretKeyPreProd = findProperty("ps2linkAwsSecretKeyPreProd") ?: ""
        val awsAccessKeyProd = findProperty("ps2linkAwsAccessKeyProd") ?: ""
        val awsSecretKeyProd = findProperty("ps2linkAwsSecretKeyProd") ?: ""

        buildConfigField("String", "CONSUMER_SECRET", "\"${consumerSecret}\"")
        buildConfigField("String", "CONSUMER_KEY", "\"${consumerKey}\"")
        buildConfigField("String", "ACCESS_TOKEN", "\"${accessToken}\"")
        buildConfigField("String", "ACCESS_TOKEN_SECRET", "\"${accessTokenSecret}\"")

        productFlavors {
            getByName("preprod") {
                buildConfigField("String", "APP_CENTER_ID", "\"${appCenterIdPreProd}\"")
                buildConfigField("String", "CENSUS_SERVICE_ID", "\"${serviceIdPreProd}\"")
                resValue("string", "aws_access_key", "${awsAccessKeyPreProd}")
                resValue("string", "aws_secret_key", "${awsSecretKeyPreProd}")
            }
            getByName("prod") {
                buildConfigField("String", "APP_CENTER_ID", "\"${appCenterIdProd}\"")
                buildConfigField("String", "CENSUS_SERVICE_ID", "\"${serviceIdProd}\"")
                resValue("string", "aws_access_key", "${awsAccessKeyProd}")
                resValue("string", "aws_secret_key", "${awsSecretKeyProd}")
            }
        }
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
}

dependencies {
    implementation(project(":framework:assert"))
    implementation(project(":framework:crashhandler"))
    implementation(project(":framework:core"))
    implementation(project(":framework:core-compose"))
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

    implementation(project(":auraxiscontrolcenter:appcore"))
    implementation(project(":auraxiscontrolcenter:app-frontend"))
    implementation(project(":auraxiscontrolcenter:core-models"))
    implementation(project(":auraxiscontrolcenter:deployable-models"))
    implementation(project(":auraxiscontrolcenter:ui"))

    implementation(AndroidX.appCompat)
    implementation(AndroidX.activity.ktx)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.paging.runtimeKtx)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.preference)
    implementation(AndroidX.preference.ktx)
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)
    implementation(AndroidX.work.runtimeKtx)
    implementation(AndroidX.work.runtime)
    implementation(Google.android.material)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.test.espresso.idlingResource)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.material.icons.core)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.compose.runtime.liveData)
    implementation(KotlinX.datetime)
    implementation(KotlinX.collections.immutable)
    implementation(KotlinX.serialization.json)
    implementation("com.microsoft.appcenter:appcenter:_")
    implementation("com.microsoft.appcenter:appcenter-crashes:_")
    implementation(Ktor.client.android)
    implementation("org.ocpsoft.prettytime:prettytime:_")
    implementation(Square.sqlDelight.drivers.android)

    testImplementation(project(":framework:test"))

    androidTestImplementation(AndroidX.test.runner)
    androidTestImplementation(AndroidX.test.espresso.intents)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(AndroidX.test.uiAutomator)
    androidTestImplementation( "com.microsoft.appcenter:espresso-test-extension:_")
}
