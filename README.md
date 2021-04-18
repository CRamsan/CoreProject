# Project Repo

This is the mono-repo that holds all the code for several projects. The reason for going with a mono-repo was to make code-sharing easier and reducing maintenance cost.

## Current Projects
| Project | Builds |
| --- | ----------- |
| [Auraxis Control Center](auraxiscontrolcenter/) | [![Build Status](https://dev.azure.com/CRamsan/AuraxisControlCenter/_apis/build/status/AuraxisControCenter?branchName=master)](https://dev.azure.com/CRamsan/AuraxisControlCenter/_build/latest?definitionId=6&branchName=master) |
| [Safe for my pets](petproject/) | Android: [![Build Status](https://dev.azure.com/CRamsan/PetProject/_apis/build/status/PetProject-Android?branchName=master)](https://dev.azure.com/CRamsan/PetProject/_build/latest?definitionId=3&branchName=master)<br/>Webservice: [![Build Status](https://dev.azure.com/CRamsan/PetProject/_apis/build/status/PetProject-Webservice?branchName=master)](https://dev.azure.com/CRamsan/PetProject/_build/latest?definitionId=8&branchName=master) |
| [Doom](/doom) | Android: [![Build Status](https://dev.azure.com/CRamsan/Doom-Project/_apis/build/status/Doom-Project_Android?branchName=master)](https://dev.azure.com/CRamsan/Doom-Project/_build/latest?definitionId=11&branchName=master)<br/>Desktop: [![Build Status](https://dev.azure.com/CRamsan/Doom-Project/_apis/build/status/Doom-Project_Desktop?branchName=master)](https://dev.azure.com/CRamsan/Doom-Project/_build/latest?definitionId=12&branchName=master) |
| [Framework](framework/) | |

# Framework
A lot of the code is written in a shared module called **Framework** that abstract a lot of complexities out of the client apps.
 - [Framework](framework/)
 - [Root gradle file](/build.gradle), [properties](gradle.properties) and [settings](settings.gradle)

# Documentation
You can find detailed documentation in the [wiki](https://github.com/CRamsan/CoreProject/wiki/).

## Getting started
 
### Prerequisites
- JDK 11
- Gradle 6.7.1(installed from the wrapper)
- Android Studio 2020.3.1 Canary 1(Stable) / Android Studio 2020.3.1 Canary 2 (Early testing)
- IntelliJ is not currently supported(issue #139)

### Building the code
To build the entire project and execute all the tests run: `./grdlew release`.

### Build 
To just build the code, run `./grdlew assembleDebug` for Android projects or `./gradlew build` for JVM projects. 

### Formatting
The code is configured to follow a pre-defined format style. This is enforced by the build process, so it is important to always follow the format otherwise the build process will fail. To automatlly fix any format issues, just run `./gradlew ktlintf`.

### Testing
You can read more about testing in the [Testing](https://github.com/CRamsan/CoreProject/wiki/Testing) page.

### Create a new module
If you want to start a new project and need module, look at the [New Module](https://github.com/CRamsan/CoreProject/wiki/New-Module) page for some examples about how to get started.
