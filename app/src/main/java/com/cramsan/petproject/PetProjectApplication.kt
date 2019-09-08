package com.cramsan.petproject

import android.app.Application
import android.content.Context
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.assert.implementation.AssertUtilInitializer
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.EventLoggerInitializer
import com.cramsan.framework.logging.implementation.PlatformLogger
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.PlatformPreferences
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesInitializer
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer
import com.cramsan.petproject.appcore.storage.implementation.ModelStoragePlatformInitializer
import com.google.android.gms.ads.MobileAds
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
        bind<HaltUtilInterface>() with singleton {
            val haltUtil by kodein.newInstance { HaltUtil(instance()) }
            haltUtil
        }
        bind<ThreadUtilInterface>() with singleton {
            val threadUtil by kodein.newInstance { ThreadUtil(instance(), instance()) }
            threadUtil
        }
        bind<AssertUtilInterface>() with singleton {
            val initializer = AssertUtilInitializer(BuildConfig.DEBUG)
            val assertUtil by kodein.newInstance { AssertUtil(initializer, instance(), instance()) }
            assertUtil
        }
        bind<ModelStorageInterface>() with singleton {
            val context: Context = this@PetProjectApplication
            val modelStorage by kodein.newInstance {
                ModelStorage(ModelStorageInitializer(ModelStoragePlatformInitializer(context)),
                instance(),
                instance()) }
            modelStorage
        }
        bind<PreferencesInterface>() with singleton {
            val context = this@PetProjectApplication
            val preferencesInitializer = PreferencesInitializer(PlatformPreferences(context))
            val preferences by kodein.newInstance {
                Preferences(preferencesInitializer)
            }
            preferences
        }
        bind<ModelProviderInterface>() with singleton {
            val modelProvider by kodein.newInstance {
                ModelProvider(instance(),
                    instance(),
                    instance(),
                    instance()) }
            modelProvider
        }
        MobileAds.initialize(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        eventLogger.log(Severity.INFO, classTag(), "onCreate called")
    }
}
