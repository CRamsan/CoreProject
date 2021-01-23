# Testing

Testing is one of the core steps in the development process. This project aims to ease the process to create unit tests.

## Getting started
Most of the code for enabling testing can be found in `:framework:testing`. This module will provide a single API to create unit tests and internally it will ensure to use the correct platform abstractions to execute those tests. Some of the main tools used for testing are:

 - [KotlinTest](https://kotlinlang.org/api/latest/kotlin.test/): Kotlin API for testing and asserting. This API is multiplaform and allows to write unit-tests in a shared common package.
 - [Junit](https://junit.org/junit4/): Main unit-test framework used for the JVM and Android targets.
 - [Mockk](https://mockk.io/): This is the preferred library for mocking given that it supports kotlin multiplatform.

## Running Tests

### Multiplatform 

Run ` check` to execute all tests across all supported targets. To execute only the JVM test in a MPP project you can use the `jvmTest` task. Keep in mind that pure JVM projects use a different task to run tests. To execute Android tests in a MPP project, look at the next section.

### Android

Run `test` to execute all tests in the Android target. This will run the tests for each flavor. If you want to run for a especfic flavour you can also run `test<Flavour>UnitTest`. On Android, there is also an *androidTest* target that contains test that are executed on an Android envriroment(simuator or physical device). As part of the `test` task, only the unit tests are part of the *test* target are executed. 

Running *androidTest* tests is out of the scope of this document.

### JVM

Run  `jvmTest` to run all tests.

## Writting tests

This is the basic structure of a test class.

```
class ClassImplTest : TestBase() {

    @MockK
    lateinit var log: EventLoggerInterface

    @MockK(relaxUnitFun = true)
    lateinit var dao: DAO

    lateinit var instanceToTest: SomeInterface

    @BeforeTest
    override fun setupTest() {
        instanceToTest = Instance(
            log,
            assert,
        )
    }

    /**
     * Basic senario
     */
    @Test
    fun `testing basic scenario`() = runBlockingTest {
        every { dao.getData() } returns emptyList()
        assertNotNull(instanceToTest.getResult())
    }
}
```

- **TestBase**: Implements some logic to configure the test enviroment. All test suites should inherit this class to ensure consistency.
- **@Mockk**: Using this annotation allows an easy way to declare the type of mock to use. By default, all mocks will throw an exception for any interaction. You can override this behaviour by using `relaxUnitFun = true`. 
- **setupTest**: Is the base function that will configure the test-case. This function is executed once before each test runs.
- **every/coEvery**: This is the main approach for mocking objects in Mockk. For more detailed information, look at the [Mockk documentation](https://mockk.io/).
- **assert functions**: These are the Kotlin test APIs for assertion. They are convenient to use as they are part of the common API.
- **Test naming**: Kotlin allows for using string literals in the function name, this is very convenient to use as test names.


