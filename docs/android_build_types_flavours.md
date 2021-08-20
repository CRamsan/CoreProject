# Android build types and flavours

For some introduction on build variants, read the Google docs [here](https://developer.android.com/studio/build/build-variants). This documents how build variants are using within this project.

## Build Types

There are two build types that are the standard on mobile development, **debug** and **release** types. The main difference is that the **debug** build type has debugging enabled and no compile-time optimizations. This makes the **debug** build variant the one that is most commonly used by programmers when developing and debugging apps. This build type also is signed during the build process using a **debug** certificate, that tells the other tools that this app is not intended for customer use.

In contrast, the **release** build type can contain compile time optimizations such as the ones provided by [R8](https://developer.android.com/studio/build/shrink-code). This will make the **release** app much faster and responsive as compared to the **debug** type. The **release** build type is not signed during the build process and it is up to the developer to sign it using a **release** certificate.

In short you can say that the **release** build type is what the customer will get while the **debug** build type is used by developers.

## Product Flavours

Product flavours are not so focused on the compilation or release of the app and instead they are used for different ways to provide features in the app. An example of common uses of product flavours would be a demo/full version of an app.

For the purposes of our project, I am proposing we split our project into two flavours: **preprod** and **prod**.
The **prod** flavour would be used for all resources and configurations that are intended to be provided to the final end-user. The **preprod** flavour would be used to provided some extended functionality for developers and testers. Some of such functionalities would be access to a debug menu and the option to configure the back-end endpoints that the app uses.

## Approach
The build will produce an APK for each combination of build types and product flavours. The result will be four APKs:
 - app-preprod-debug.apk
 - app-preprod-release.apk
 - app-prod-debug.apk
 - app-prod-release.apk

This chart describes the intended use of each build type:

|         | PreProd    | Prod |
|--       |--          |--|
| Debug   | Regular app development and debugging | **Rarely used.** |
| Release | Distributed to internal testers.<br> This build would not be part of a release<br> pipeline as it is never intended to reach users.| Distributed to users across official channels. |

The apps will roughly be used as this:

1. The developer will configure their local environment and set both **PreProd** and Prod values to the **PreProd** values.
2. These values will be used locally to develop and debug the app. When the changes are ready, they will be committed to git.
3. The continuous integration is already configured to provide the appropriate values for **PreProd** and **Prod** flavours.
4. The continuous integration pipeline will pick up the change and build the matrix of APKs. The **Debug** APKs will be retained in the build pipeline for a limited period of time. The **Release** APKs will be signed signed with their respective keys.
5. Then **PreProd** APK will be release to internal testers.
6. The **Prod** APKs will be released to the first track of the PlayStore.
