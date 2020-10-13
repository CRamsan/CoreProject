The framework module is a shared project that defines a common shared library that provides basic functionaility for mobile apps. This project is divided in a set of modules that can be consumed as needed.

## Module
A module is the main unit of logic and it is composed of the public interface and the common-code implementation. Some modules require a platform-specific initializer.

## Existing Modules

#### [Assert](src/assert/)
This module allows to have functions for asserting on condiftions at run-time. If the condition is not met, the tread will be stopped and the user will be notified about the failure. This module can be configured to be disabled(for example in Prod). 

#### [Crash Handler](src/crashhandler/)
THe crash handler is a module that will take care to catch crashes so they can be reported.

#### [Halt](src/halt/)
This module exposes an API to pause and resume a thread. This is mostly to be used as a development tool and as a dependency for other modules. This module also has an API to crash the app.

#### [Logging](src/logging/)
This module exposes a single API to log events. This module can be configured so that events logged as WARNING or ERROR are automatically reported as metrics and can halt the thread.

#### [Metrics](src/metrics/)
This module provides an API to track metrics regarding to user engagements.

#### [Preferences](src/preferences/)
This module has a simple API to easily persist data in local storage. It was designed to be used for simple operations that do not require high performance.

#### [Thread](src/thread/)
This module provides some utility methods around Threads, like determining if the current thread is the UI or background thread.