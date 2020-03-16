# Design & Architecture
This module aims to use a scalable design and good software patterns. The code that is written for this module should be reusable on future projects. I am also targetting to develop software that can run natively on as many platforms as possible without inpacting the end-user experiene. I will not favor writting cross-platform code if it will negatively impact the development process or the resulting product.

## Target Platforms
- I am targetting Android and JVM first.
- iOS and native modules are future features.

## Language and tools
This module follows the project's overall guidance to use Kotlin/Kotlin MP and Gradle.

## Architecture
This module provides the different modules that can be consumed by higher level components. The Framework is a set of packages that written in Kotlin MP and therefore follow the [Kotlin MP architecture](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html) of a *common* source set plus and additional source set for each target platform. 

The goal of having the code divided in *modules* is:
- Encapsulate a single functionality that can be consumed an upstream project.
- Decouple an upstream project's dependency from a particular external dependency.
- Clear dependency list for a single module(Discourage accessing global/static variables)
- Similar API surface across modules

### Module
A module is the main unit of logic and it is composed of five main elements, the manifest, the interface, implementation, initializer and platform-initializer.

#### Manifest(*ModuleName*Manifest.kt)
Empty interface that is used to provide compile time checks to ensure all the elements belong to the same Module.

#### Public interface(*ModuleName*Interface.kt)
This is the interface that should be used when accessing a module.

#### Implementation(*ModuleName*.kt)
Implementation in the common Kotlin code for this module. It can delegate work to a platform-specific delegate that also implements the public interface.

#### Initializer(*ModuleName*Initializer.kt)
Provides the parameters needed to configure this initializer. It also takes as an argument, an instance of a PlatformInitializer to provide any parameters that are platform specific.

#### Platform Initializer(*ModuleName*PlatformInitializerInterface.kt)
This interface will be implemented on the platform-specific source-set to handle parameters and logic that specific to each platform.