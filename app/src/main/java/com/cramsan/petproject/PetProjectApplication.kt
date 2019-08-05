package com.cramsan.petproject

import android.app.Application
import android.content.Context
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilInitializer
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.logging.implementation.PlatformLogger
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilInitializer
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderInitializer
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderPlatformInitializer
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import org.kodein.di.newInstance

class PetProjectApplication : Application(), KodeinAware {

    private val eventLogger: EventLoggerInterface by instance()

    override val kodein = Kodein.lazy {
        import(androidXModule(this@PetProjectApplication))

        bind<EventLoggerInterface>() with singleton {
            val severity: Severity = when (BuildConfig.DEBUG) {
                true -> Severity.DEBUG
                false -> Severity.INFO
            }
            EventLogger(EventLoggerInitializer(severity, PlatformLogger()))
        }
        bind<ThreadUtilInterface>() with singleton {
            val threadUtil by kodein.newInstance { ThreadUtil(ThreadUtilInitializer(), instance()) }
            threadUtil
        }
        bind<HaltUtilInterface>() with singleton {
            val haltUtil by kodein.newInstance { HaltUtil(HaltUtilInitializer(), instance()) }
            haltUtil
        }
        bind<ModelStorageInterface>() with singleton {
            val context: Context = this@PetProjectApplication
            val modelStorage by kodein.newInstance {
                ModelStorage(ModelStorageInitializer(ModelStoragePlatformInitializer(context)),
                instance(),
                instance()) }
            modelStorage
        }
        bind<ModelProviderInterface>() with singleton {
            val modelProvider by kodein.newInstance {
                ModelProvider(ModelProviderInitializer(ModelProviderPlatformInitializer()),
                    instance(),
                    instance(),
                    instance()) }
            modelProvider
        }
    }

    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, classTag(), "onCreate called")
    }
}
