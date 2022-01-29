The framework module is a shared project that defines a common shared library that provides basic functionaility across all projects. This project is divided in a set of modules that can be consumed as needed.

## Getting started

Now you can compile all the modules and run their tests with `./gradlew framework:release`.

Since there is no way to interact directly with the framework modules, ensuring that unit tests and running and passing is of up-most importance. 
It is also recommended to get familiar with the [Framework-Samples](../framework-samples/) project, as those are very lightweight targets that also consume this modules. 

## Module
A module is the main unit of logic and it is composed of the public interface and the common-code implementation. Some modules also provide extended support for each platform.

## Current Modules

#### [Assert](src/assert/)
This module allows to have functions for asserting on condiftions at run-time. If the condition is not met, the tread will be stopped and the user will be notified about the failure. This module can be configured to be disabled(for example in Prod). 

### [Core](src/core/)
This module contains a diverse set of classes that should be used as a platform to build other projects. **Other framework modules should not depend on this module**.

#### [Crash Handler](src/crashhandler/)
THe crash handler is a module that will take care to catch crashes so they can be reported.

#### [Halt](src/halt/)
This module exposes an API to pause and resume a thread. This is mostly to be used as a development tool and as a dependency for other modules. This module also has an API to crash the app.

### InterfaceLib(src/interfacelib) ###
As a way to decouple the interfaces from their implementations, this module exposes all the public Framework APIs. Each client project can then consume only the module that they need.
This approach allows project to not have to depend on project that they dont't need only because the interface was required.

#### [Logging](src/logging/)
This module exposes a single API to log events. This module can be configured so that events logged as WARNING or ERROR are automatically reported as metrics and can halt the thread.

#### [Metrics](src/metrics/)
This module provides an API to track metrics regarding the internal execution of the application.

#### [Preferences](src/preferences/)
This module has a simple API to easily persist data in local storage. It was designed to be used for simple operations that do not require high performance.

#### [RemoteConfig](src/remoteconfig/)
Module that provides a solution to fetch configuration from a remote endpoint.

#### [Test](src/test/)
This module provides a core set of tools for testing our code. This module should be consumed as a `testImplementation` or `androidTestImplementation` dependency.

#### [Thread](src/thread/)
This module provides some utility methods around Threads, like determining if the current thread is the UI or background thread.

#### [UserEvents](src/userevents/)
Module for recording events around user-engagement.

#### [Utils](src/utils/)
Sometimes there are some utilities or extensions that do not need their entire module. If this util has little or no dependencies, then it can be kept here so it can be reused.