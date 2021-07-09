# Project Repo

This mono-repo holds the code for several projects that I manage. The reason for going with a mono-repo was to make code-sharing easier and reducing maintenance cost. You can find more information about the projec's design on the [design](docs/design.md) page.

## Current Projects
| Project | Builds |
| --- | ----------- |
| [Auraxis Control Center](auraxiscontrolcenter/README.md) | Android: [![Build Status](https://dev.azure.com/CRamsan/AuraxisControlCenter/_apis/build/status/AuraxisControCenter?branchName=master)](https://dev.azure.com/CRamsan/AuraxisControlCenter/_build/latest?definitionId=6&branchName=master) <br/>Service: [![Build Status](https://dev.azure.com/CRamsan/AuraxisControlCenter/_apis/build/status/AuraxisControlCenter_NodeJs?branchName=master)](https://dev.azure.com/CRamsan/AuraxisControlCenter/_build/latest?definitionId=14&branchName=master) |
| [Safe for my pets](petproject//README.md) | Android: [![Build Status](https://dev.azure.com/CRamsan/PetProject/_apis/build/status/PetProject-Android?branchName=master)](https://dev.azure.com/CRamsan/PetProject/_build/latest?definitionId=3&branchName=master)<br/>Webservice: [![Build Status](https://dev.azure.com/CRamsan/PetProject/_apis/build/status/PetProject-Webservice?branchName=master)](https://dev.azure.com/CRamsan/PetProject/_build/latest?definitionId=8&branchName=master) |
| [Doom](doom/README.md) | Android: [![Build Status](https://dev.azure.com/CRamsan/Doom-Project/_apis/build/status/Doom-Project_Android?branchName=master)](https://dev.azure.com/CRamsan/Doom-Project/_build/latest?definitionId=11&branchName=master)<br/>Desktop: [![Build Status](https://dev.azure.com/CRamsan/Doom-Project/_apis/build/status/Doom-Project_Desktop?branchName=master)](https://dev.azure.com/CRamsan/Doom-Project/_build/latest?definitionId=12&branchName=master) |
| [Framework](framework/README.md) | |

# Framework
A lot of the code is kept in a shared module called **Framework** that abstract a lot of complexities out of the client apps.
 - [Framework](framework/)
 - [Root gradle file](/build.gradle), [properties](gradle.properties) and [settings](settings.gradle)

## Getting started
 
### Prerequisites
- JDK 11
- Gradle 7.0(installed from the wrapper)
- Android Studio 2020.3.1 Canary 15 ([Download page](https://developer.android.com/studio/archive))
- ~~IntelliJ~~ Currently not supported (issue #139)

### Building the code
To build all the projects and execute all tests run: `./grdlew release`.

### Build 
To just build the code, run `./grdlew assembleDebug` for Android projects or `./gradlew build` for other projects. For example `./gradlew petproject:app assembleDebug` for the Petproject app.

### Formatting
The code is configured to follow a pre-defined format style. This is enforced by the build process, so it is important to always follow the format otherwise the build process will fail. To automatlly fix any format issues, just run `./gradlew ktlintf`. If this task fails, you will have to manually fix the issue.

### Testing
You can read more about testing in the [Testing](docs/testing) page.

### Create a new module
If you want to start a new project and need a new module, look at the [New Module](docs/new_module) page for some examples about how to get started.

### More information
To learn more about the project, please look at the [documentation](docs/).
