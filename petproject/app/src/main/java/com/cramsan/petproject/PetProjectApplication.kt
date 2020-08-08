package com.cramsan.petproject

import android.app.Application
import android.content.Context
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.cramsan.framework.crashehandler.implementation.AppCenterCrashHandler
import com.cramsan.framework.crashehandler.implementation.CrashHandler
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerAndroid
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.AppCenterMetrics
import com.cramsan.framework.metrics.implementation.Metrics
import com.cramsan.framework.metrics.implementation.MetricsErrorCallback
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilAndroid
import com.cramsan.petproject.appcore.feedback.FeedbackManagerDAO
import com.cramsan.petproject.appcore.feedback.FeedbackManagerInterface
import com.cramsan.petproject.appcore.feedback.implementation.FeedbackManager
import com.cramsan.petproject.appcore.model.feedback.Feedback
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.ProviderConfig
import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageAndroidProvider
import com.cramsan.petproject.work.DailySyncManager
import com.cramsan.petproject.work.ScheduledSyncManager
import com.microsoft.appcenter.AppCenter
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.newInstance
import org.kodein.di.singleton

class PetProjectApplication : Application(), DIAware {

    private val eventLogger: EventLoggerInterface by instance()
    private val crashHandler: CrashHandlerInterface by instance()
    private val metrics: MetricsInterface by instance()
    private val syncManager: ScheduledSyncManager by instance()

    override val di = DI.lazy {
        import(androidXModule(this@PetProjectApplication))

        bind<CrashHandlerInterface>() with singleton {
            CrashHandler(AppCenterCrashHandler())
        }
        bind<EventLoggerErrorCallbackInterface>() with singleton {
            MetricsErrorCallback(instance())
        }
        bind<MetricsInterface>() with singleton {
            Metrics(AppCenterMetrics())
        }
        bind<EventLoggerInterface>() with singleton {
            val severity: Severity = when (BuildConfig.DEBUG) {
                true -> Severity.DEBUG
                false -> Severity.INFO
            }
            EventLogger(severity, instance(), LoggerAndroid())
        }
        bind<HaltUtilInterface>() with singleton {
            HaltUtil(HaltUtilAndroid())
        }
        bind<ThreadUtilInterface>() with singleton {
            val threadUtil by di.newInstance { ThreadUtil(ThreadUtilAndroid(instance())) }
            threadUtil
        }
        bind<AssertUtilInterface>() with singleton {
            val assertUtil by di.newInstance { AssertUtil(BuildConfig.DEBUG, instance(), instance()) }
            assertUtil
        }
        bind<ModelStorageInterface>() with singleton {
            val context: Context = this@PetProjectApplication
            val modelStorageDAO = ModelStorageAndroidProvider(
                context
            ).provide()
            val modelStorage by di.newInstance {
                ModelStorage(
                    modelStorageDAO,
                    instance(),
                    instance()
                )
            }
            modelStorage
        }
        bind<PreferencesInterface>() with singleton {
            val context = this@PetProjectApplication
            val preferences by di.newInstance {
                Preferences(PreferencesAndroid(context))
            }
            preferences
        }
        bind<ProviderConfig>() with singleton {
            ProviderConfig(
                getString(R.string.provider_config_plants_url),
                getString(R.string.provider_config_mainname_url),
                getString(R.string.provider_config_commonname_url),
                getString(R.string.provider_config_description_url),
                getString(R.string.provider_config_family_url),
                getString(R.string.provider_config_toxicities_url)
            )
        }
        bind<ModelProviderInterface>() with singleton {
            val modelProvider by di.newInstance {
                ModelProvider(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance()
                )
            }
            modelProvider
        }
        bind<FeedbackManagerInterface>() with singleton {
            class DummyDAO : FeedbackManagerDAO {
                override fun submitFeedback(feedback: Feedback) {
                    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                }
            }
            val feedbackManager by di.newInstance {
                FeedbackManager(
                    DummyDAO(),
                    instance(),
                    instance()
                )
            }
            feedbackManager
        }
        bind<ScheduledSyncManager>() with singleton {
            DailySyncManager()
        }
    }

    override fun onCreate() {
        super.onCreate()
        internalInstance = this
        eventLogger.log(Severity.INFO, "PetProjectApplication", "onCreate called")
        AppCenter.start(this, "1206f21f-1b20-483f-9385-9b8cbc0e504d")
        crashHandler.initialize()
        metrics.initialize()
        syncManager.startWork()
    }

    companion object {
        private lateinit var internalInstance: PetProjectApplication
        fun getInstance(): PetProjectApplication = internalInstance
    }
}
