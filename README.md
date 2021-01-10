# Project Repo

This is the mono-repo that holds all the code for all my projects. The reason for going with a mono-repo was to make code-sharing easier and reducing maintenance cost.

## Current Projects
 - [Auraxis Control Center](auraxiscontrolcenter/)
 - [Safe for my pets](petproject/)
 - [Framework](framework/)

# Framework
 - [Framework](framework/)
 - [Root gradle file](/build.gradle), [properties](gradle.properties) and [settings](settings.gradle)

# Documentation
 - [Design](/DESIGN.md)
 - [CI/CD](/CONTINUOUS.md)
 
## Getting started
 
### Prerequisites
- JDK 11
- Gradle 6.7.1(installed from the wrapper)
- Android Studio 2020.3.1 Canary 1(Stable) / Android Studio 2020.3.1 Canary 2 (Early testing)
- IntelliJ is not currently supported(#139)

To build the entire project run: `./grdlew release`

