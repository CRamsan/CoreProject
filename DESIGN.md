# Design & Architecture

From the beggining, this project aims to use a scalable design and good software patterns. A lot of the code that is written for this project should be reusable on future projects. I am also targetting to develop software that can run natively on as many platforms as possible without inpacting the end-user experiene. I will not favor writting cross-platform code if it will negatively impact the development process or the resulting product.

## Target Platforms
- For the mobile app I am targetting Android first with a possible iOS app in the future.
- There will be other components such as a possible service to provide an API or a GUI to manage the databases. This components should run on Linux, Mac or Windows.

## Language and tools
To be able to write my code once and run it on as many platforms as possible I decided to use Kotlin. Kotlin has the benefit of producing JVM code that can be run on most platforms(except iOS). For iOS we can use [Kotlin Native](https://kotlinlang.org/docs/reference/native-overview.html) to develop a solution that is native to iOS. To allow for reusable code between both iOS and JVM I am using [Kotlin Multiplatform](https://kotlinlang.org/docs/reference/multiplatform.html). For areas that target iOS specific use-cases(such as UI) we can use Swift or Objective-C.

The build tool of choice will be [Gradle](https://kotlinlang.org/docs/reference/using-gradle.html) due to it's strong support of Java, Kotlin and Android. For iOS, we can use Gradle to produce an XCode project that loads the native libraries, from that point onwards the iOS development can be done through XCode and it's tools. 

## Architecture
To maximise code reusability and prevent strong coupling of business logic, this project works on three layers the Framework, AppCore and Front-Ends(App/WebService). Each of the layers is defined as a Gradle subproject that depends on the previous layer. Both the Framework and AppCore layers are Kotlin MP projects and therefore follow the [Kotlin MP architecture](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html) of a *common* source set plus and additional source set for each target platform. The Front-End projects will use architecture's that best fit their platforms. You can find more information on their respective design doc.

The Framework and AppCore projects will be structured internally as a set of top-level *modules* that expose their behaviour through interfaces. These modules will be designed to be used as singletons the project that consumes them will be responsible to create the singleton and to inject the right dependencies during initialization.

The goal of having the code divided in *modules* is:
- Encapsulate a single functionality that can be consumed an upstream project.
- Decouple an upstream project's dependency from a particular external dependency.
- Clear dependency list for a single module(Discourage accessing global/static variables)
- Similar API surface across modules


### Framework
This project holds all the business logic that can be reused on other projects. Usually this pieces of code are fairly light-weight, things like functions for logging, asserting, persisting key-value pairs. Currently this project is part of the overall PetProject, but in the future this project will be moved outside of this project.

### AppCore
This project holds the main business logic for PetProject. Areas such as retrieving models, persisting them and transforming them are the main focus of this project. This project does not manage UI components. 

### Front-Ends
The front-ends are the projects that will import the modules from the Framework and AppCore modules and inject all required dependencies. Each front-end project targets will follow the best architecture for their targets, for example MVVC for Android and MVC for the Spring WebService.

## Code Quality
To ensure continous code quality, this project will follow this guidelines:
- Rely heavily on Kotlin's nullability types
- Use code analysis to catch possible issues
- All business logic should have UnitTests
- The UTs should have a high code-coverage, the target is still TBD.
- Passing code analysis, code style and unit tests is required for a successful build.

### Unit Testing
Given most of the code is kotlin multiplatform, there is some caveats to run the same UTs for all platforms. All the UTs should be defined in the *commonTest* source set so that they all can be run on all platforms. Each platform will then call each of the UT in *commonTest*. For example a JVM target can run all UTs through JUnit, while Android can use Robolectric and iOS can use XCTest.

### Code Coverage
TBS

### Code Analysis
To ensure we are following correct software patters, we will use a code analysis tool to analyse the code at build-time. For this we will use the [Detekt](https://github.com/arturbosch/detekt) tool.

### Code Style
All the projects will follow the same code style and there will be a task to format code. The tool of choice will be [ktlint](https://github.com/pinterest/ktlint).
