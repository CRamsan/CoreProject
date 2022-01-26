package com.cramsan.petproject.azurefunction

import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.thread.implementation.ThreadUtilJVM
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageJdbcProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Class that holds all dependencies to be used for this project.
 * Dependencies are modeled so that they are lazily initialized.
 */
class DependenciesConfig {

    /**
     * Instance of [EventLoggerInterface]
     */
    val eventLogger: EventLoggerInterface by lazy {
        val impl = EventLoggerImpl(Severity.INFO, null, LoggerJVM())
        EventLogger.setInstance(impl)
        impl
    }

    /**
     * Instance of [HaltUtil]
     */
    val haltUtil: HaltUtil by lazy {
        HaltUtilImpl(HaltUtilJVM())
    }

    /**
     * Instance of [AssertUtilInterface]
     */
    val assertUtil: AssertUtilInterface by lazy {
        AssertUtilImpl(false, eventLogger, haltUtil)
    }

    /**
     * Instance of [ThreadUtilInterface]
     */
    val threadUtil: ThreadUtilInterface by lazy {
        ThreadUtilImpl(ThreadUtilJVM(eventLogger, assertUtil))
    }

    /**
     * Instance of [ModelStorageInterface]
     */
    val modelStorage: ModelStorageInterface by lazy {
        val inputStream: InputStream? = javaClass
            .classLoader.getResourceAsStream("PetProject.sql")

        val tempFile = File.createTempFile("hello", ".tmp")
        println("Temp file On Default Location: " + tempFile.absolutePath)

        if (inputStream == null) {
            throw UnsupportedOperationException("Could not get InputStream")
        }

        val buffer = ByteArray(BUFFER_SIZE)
        val outStream: OutputStream = FileOutputStream(tempFile)

        var len: Int = inputStream.read(buffer)
        while (len != -1) {
            outStream.write(buffer, 0, len)
            outStream.flush()
            len = inputStream.read(buffer)
        }

        outStream.close()
        inputStream.close()

        eventLogger.log(Severity.ERROR, "TEST", tempFile.absolutePath)

        val dbPath: String? = tempFile.absolutePath

        if (dbPath == null) {
            throw UnsupportedOperationException("Path for sqlite is null")
        }
        val modelStorageDAO = ModelStorageJdbcProvider(
            dbPath
        ).provide()
        ModelStorage(
            modelStorageDAO,
            eventLogger,
            threadUtil
        )
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}
