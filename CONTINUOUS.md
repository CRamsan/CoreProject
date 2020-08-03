# Continuous Delivery

Our current solution for CD is through Azure DevOps and Pipelines. The goal is to have every change validated through automated testing and then valid builds are automatically published. 

Continuous delivery has two parts, the build pipeline and the release pipeline. The build pipeline will checkout the code, build it, validate it and even publishing it. The release pipeline is a secondary pipeline that can create a flow to define how artifacts can be released to users.

## Setup

A module that wants to be CD needs to have a target for unit tests and another target for lint checks. The build pipeline is defined in a yaml file located in a cicd/ folder. For example the pipelines for Petproject are located in [/petproject/cicd/](/petproject/cicd/).

First configure when will your pipeline trigger.
```yaml
trigger:
  // Preferably, configure your pipeline to 
  // only trigger for files that are relevant to your project
  paths:
    include:
    // First you need to include the root level
    // files since all projects depent on them 
    - build.gradle
    - gradle.properties
    - settings.gradle

    // Include folders that contain common code
    - framework/*

    // Add the folder relevant to your project
    - petproject/*
```

Then configure the build step to launch the required build, test and validation steps.
```yaml
steps:
- task: Gradle@2
  inputs:
    workingDirectory: ''
    gradleWrapperFile: 'gradlew'
    gradleOptions: '-Xmx3072m'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '1.11'
    jdkArchitectureOption: 'x64'
    publishJUnitResults: true
    testResultsFiles: '**/TEST-*.xml'
    checkStyleRunAnalysis: true
    // Configure all the required steps
    tasks: 'petproject:app:build petproject:app:assembleAndroidTest'

- task: AndroidSigning@3
  inputs:
    // Select the APK to sign
    apkFiles: '**/app-release-unsigned.apk'
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
    // Here there are two groupIds, one for a distribution group 
    // and another for the Collaborators default group
    distributionGroupId: 'acb71603-515c-4870-86e7-625460eecc4d, 00000000-0000-0000-0000-000000000000'
```

The second step is to have a [Release Pipeline](https://docs.microsoft.com/en-us/azure/devops/pipelines/release/?view=azure-devops) to manage how the created artifacts are released to users. As of now, those pipelines are managed only through the Azure DevOps web UI. The reason for having to have a different pipeline is that not all builds need to be released to users. By having this separate pipeline, we can control how release builds are distributed across to different locations and stages.  