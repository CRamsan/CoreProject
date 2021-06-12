Creating new app in AppCenter

The main distribution mechanism we currently use is AppCenter. You can find all the projects in https://appcenter.ms/users/cramsan/. Here we will describe how to create a fully working app entry that tracks metrics and crashes. 

## Create the application

1. Go to https://appcenter.ms/users/cramsan/applications
1. On the top-right, selct "Add new app"
1. Fill out the form and save
1. You will be taken to the "Overview" tab. In the "Getting started" section, on step 2 you will find the application ID that we will use in the next steps.

```
AppCenter.start(getApplication(), "1a0c860e-1c51-41a0-b176-051f813605da",
                  Analytics.class, Crashes.class);
``` 
ApplicationId is **1a0c860e-1c51-41a0-b176-051f813605da**

## Distribution

### Non-Production

If you are configuring an non-production application, usually it is not required to further configure the distribution channels. By default there is a group created called **Collaborators**, and new build will be uploaded to this group. Anyone who is a member of this project will be able to get access to those builds.

If you need more control over different testing groups or you need to share this application publicly through a link, you can go to **Distribute**->**Add new group**.

### Production

**Work in progress**

## Instrument the app-code

### Configure AppCenter SDK

To tie an application with the AppCenter service, you will need to start AppCenter in the app with the **ApplicationId** you got before. 

```
@HiltAndroidApp
class NewAwesomeApplication : Application() {

    @Inject
    @Named(APP_CENTER_ID)
    lateinit var appCenterId: String

    override fun onCreate() {
        super.onCreate()
        AppCenter.start(this, appCenterId)
        crashHandler.initialize()
        metrics.initialize()
        logMetric(TAG, "Application Started")
        ...
    }
```

To verify that the metrics are being recorded correctly you will want to add some test metrics and then launch the app to trigger them. Then go to the AppCenter page, **Analytics**->**Log flow** and verify that the metrics appear. It may take a few minutes for the metrics to show up.

### Inject the ApplicationID

The ApplicationId should not be hardcoded in the source-code and instead it should be injected as an argument. The recommended way is to pass it as part of the Gradle build process. By taking this approach we can set different Ids at compile time and inject them from from a Pipeline. 

To define the gradle argument, add the following to the app's `build.gradle`.

```
android {
    defaultConfig {
        def appCenterId = findProperty("newAppAppCenterId") ?: ""
        buildConfigField("String", "APP_CENTER_ID", "\"${appCenterId}\"")
    }
}
```

Then in-code you can retrieve this variable from the `BuildConfig` variable. 

```
@Provides
    @Named(APP_CENTER_ID)
    fun provideAppCenterId(): String = BuildConfig.APP_CENTER_ID
```

To set the *ApplicationId* for local development, you can set a property in `~/.gradle/gradle.properties`. 

```
  newAppAppCenterId=1a0c860e-1c51-41a0-b176-051f813605da
```

## Continious Deployment

Usually we target to have a pre-prod version of the app called **Stage** that we deploy through the **Collaborators** group. This app version is signed with a different signature as the **Release** version of the app so that testers can have both versions installed.

To ensure that testers always have access to the latest changes, we will use our CI/CD pipeline to automatically build/sign/distribute every change. Then the **Stage** and **Release** builds will be distributed through the respective channels.

### Configure build parameters

The **ApplicationId** will be provided as a Pipeline argument. This will need to be done in the Azure pipeline web console. For the following example, you would need to create a variable named **newAppAppCenterId** and the value would be **1a0c860e-1c51-41a0-b176-051f813605da**. 

```
- task: Gradle@2
  inputs:
    options: '-PappCenterId=$(newAppAppCenterId)'
    workingDirectory: ''
    gradleWrapperFile: 'gradlew'
    ...
    ...
```

### Signing and distribution

You can find an more information about our CI/CD implementation [here](https://github.com/CRamsan/CoreProject/wiki/CI-CD)

To proceed you will need the following:
 - KeyStore file
 - KeyStore password for the file
 - Alias for the key to use
 - Password for the key to use 
 - The path of the unsigned APK

With this information you can provide the parameters as secure variables of the pipeline. This is importatnt as we do not want to have secrets stored. The path to the APK will be hardcoded as it is not expected to change.

```
- task: AndroidSigning@3
  inputs:
    // Select the APK to sign
    apkFiles: '**/app-stage-unsigned.apk'
    apksign: true
    zipalign: false
    apksignerKeystoreFile: $(KeyStoreFile)
    apksignerKeystorePassword: $(KeyStorePassword)
    apksignerKeystoreAlias: $(KeyStoreAlias)
    apksignerKeyPassword: $(KeyPassword)

- task: CopyFiles@2
  inputs:
    // Select the files to move to the folder to be exported as artifacts
    sourceFolder: $(system.defaultworkingdirectory)/petproject/app/build/outputs/apk/
    contents: '**/*.apk'
    targetFolder: '$(build.artifactStagingDirectory)'

// Publish the files in build.artifactStagingDirectory as Pipeline artifacts
- task: PublishBuildArtifacts@1

// Distribute an APK to the AppCenter
- task: AppCenterDistribute@3
  inputs:
    serverEndpoint: 'App Center'
    appSlug: 'cramsan/Pet-Project-1'
    appFile: 'petproject/app/build/outputs/apk/stage/app-stage-unsigned.apk'
    symbolsOption: 'Android'
    releaseNotesOption: 'input'
    releaseNotesInput: 'Release Notes: TBD'
    destinationType: 'groups'
    // This is the GroupId for the Collaborators group
    distributionGroupId: '00000000-0000-0000-0000-000000000000'
```

### Production

**Work in progress**

After this, now every commit should generate a new build signed with the credentials you configured. The build will be available to the respective group AppCenter.