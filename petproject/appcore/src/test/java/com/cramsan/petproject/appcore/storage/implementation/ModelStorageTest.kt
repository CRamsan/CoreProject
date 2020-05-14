package com.cramsan.petproject.appcore.storage.implementation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.concurrent.Semaphore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit test. This will be executed in a mocked Android environment.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ModelStorageTest {

    private lateinit var modelStorageTest: ModelStorageCommonTest
    private lateinit var semaphore: Semaphore

    @Before
    fun setUp() {
        modelStorageTest = ModelStorageCommonTest()
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        modelStorageTest.setUp(
            ModelStorageAndroidProvider(
                appContext
            )
        )
        semaphore = Semaphore(0)
    }

    @After
    fun tearDown() {
        modelStorageTest.endTest()
    }

    @Test
    fun testGetPlants() {
        runBlocking {
            launch(Dispatchers.IO) {
                modelStorageTest.getPlants()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testGetPlant() {
        runBlocking {
            launch(Dispatchers.IO) {
                modelStorageTest.getPlant()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testGetPlantMetadata() {
        runBlocking {
            launch(Dispatchers.IO) {
                modelStorageTest.getPlantMetadata()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }

    @Test
    fun testDeleteAll() {
        runBlocking {
            launch(Dispatchers.IO) {
                modelStorageTest.deleteAll()
                semaphore.release()
            }
        }
        semaphore.acquire()
    }
}
