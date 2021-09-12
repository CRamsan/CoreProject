package com.cramsan.petproject.awslambda

import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.logging.logI
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.thread.implementation.ThreadUtilJVM
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage

class DependenciesConfig(
    plantsTableName: String,
    commonNamesTableName: String,
    mainNamesTableName: String,
    familiesTableName: String,
    toxicitiesTableName: String,
    descriptionsTableName: String,
) {

    val eventLogger by lazy { EventLoggerImpl(Severity.DEBUG, null, LoggerJVM()) }

    val haltUtil by lazy { HaltUtilImpl(HaltUtilJVM()) }

    val assertUtil by lazy { AssertUtilImpl(false, eventLogger, haltUtil) }

    val threadUtil by lazy { ThreadUtilImpl(ThreadUtilJVM(eventLogger, assertUtil)) }

    val modelStorageDDBDAO by lazy {
        val dao = DynamoDBDAO(
            plantsTableName,
            commonNamesTableName,
            mainNamesTableName,
            familiesTableName,
            toxicitiesTableName,
            descriptionsTableName,
        )
        dao.configure()
        dao
    }

    val modelStorage by lazy {
        ModelStorage(
            modelStorageDDBDAO,
            eventLogger,
            threadUtil
        )
    }

    init {
        EventLogger.setInstance(eventLogger)
        logI(TAG, "plantsTableName = $plantsTableName")
        logI(TAG, "commonNamesTableName = $commonNamesTableName")
        logI(TAG, "mainNamesTableName = $mainNamesTableName")
        logI(TAG, "familiesTableName = $familiesTableName")
        logI(TAG, "toxicitiesTableName = $toxicitiesTableName")
        logI(TAG, "descriptionsTableName = $descriptionsTableName")
    }

    companion object {
        const val TAG = "DependenciesConfig"
    }
}
