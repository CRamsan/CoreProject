# Creating a new module

In order to achieve our deisng goals to follow best practices and grow sustainably, this project makes heavy use of local plugins. You can learn more about each one of them by going to the [gradle](gradle/) folder. 

This page will give some examples for how to use these plugin to create a new glade module.

## Keep in mind

When creating a new module, make sure to add a dependency to the `release` task of your dependencies. 
```
release {
    dependsOn ':petproject:appcore:release'
}
```

You will need to add dependencies to your `buildScript`'s classpath based on the plugins that you apply. Sadly a plugin cannot configure this for you.

```
buildscript {
    repositories { ... }
    dependencies {
        classpath packages.AndroidBuildGradle
        classpath packages.KotlinGradle
        classpath packages.AndroidXNavigationSafeArgsGradle
        classpath packages.HiltGradle
    }
}
```

You cannot access an API that was applied from within a plugin. If you need access to those APIs, then you will need to declare it explicitly. For example:

**This will not work**
```
apply from: "$rootDir/gradle/app.gradle"

android { ... }
```

**But this will work**. Despite that `app.gradle` also applies the android plugin.
```
apply plugin: "com.android.application"
apply from: "$rootDir/gradle/app.gradle"

android { ... }
```

## New Android app

Examples: [Petproject](petproject/app/build.gradle), [AuraxisControlCenter](auraxiscontrolcenter/build.gradle)

```
apply plugin: "com.android.application"
apply from: "$rootDir/gradle/app.gradle"

android {
    defaultConfig {
        applicationId "com.cramsan.projectname"
        versionName "0.1"
    }
}
```

## New Pure-Android library
Examples: [AuraxisControlCenter:UI](auraxiscontrolcenter/ui/build.gradle)
```
apply plugin: "com.android.library"
apply from: "$rootDir/gradle/applib.gradle"

android {
    defaultConfig {
        versionName "1.0"
    }
}
```

## Kotlin Multiplatform library
Examples: [AuraxisControlCenter:Appcore](auraxiscontrolcenter/appcore/build.gradle), [Petproject:Appcore](petproject/appcore/build.gradle), [Framework](framework/build.gradle)

```
ext.libraryVersionCode = 1
ext.libraryVersionName = "0.1"

apply from: "$rootDir/gradle/mpp-lib.gradle"

dependencies {
    // Android-only dependencies
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation project(":framework")
            }
        }
        commonTest {
            dependencies {
                implementation project(":framework:test")
            }
        }
        jvmMain {
            dependencies {
            }
        }

        jvmTest {
            dependencies {
            }
        }
    }
}
```

## Kotlin JVM library
Examples: [Petproject:AzureFunction](petproject/azurefunction/build.gradle), [Doom:Core](doom/game/core/build.gradle)
```
apply from: "$rootDir/gradle/kotlin-jvm-lib.gradle"

group = 'com.cramsan.project'
version = '0.0.1-SNAPSHOT'

dependencies {
    // JVM dependencies
}
```

## Kotlin JVM GUI application
Examples: [Doom:Lib:Sample](doom/lib/sample/build.gradle)
```
ext.mainClassTarget = "com.cramsan.awslib.AWTRunner"

apply from: "$rootDir/gradle/kotlin-jvm-gui.gradle"

dependencies {
    // JVM Dependencies
}
```
