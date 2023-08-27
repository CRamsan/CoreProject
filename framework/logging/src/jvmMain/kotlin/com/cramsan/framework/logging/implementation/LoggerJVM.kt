package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator

/**
 * Logger that prints to stdout.
 */
class LoggerJVM(
    logToFile: Boolean,
    initializationLogLevel: Severity = Severity.INFO,
) : EventLoggerDelegate {

    private val logger: Logger
    init {
        val loggerConfiguration = Log4J2Helpers.buildConfiguration(
            logToFile,
            initializationLogLevel,
        )
        Configurator.initialize(loggerConfiguration)

        logger = LogManager.getRootLogger()
    }
    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val level = severity.toLevel()
        val logMessage = "[$tag]$message"
        logger.log(level, logMessage, throwable)
        throwable?.let {
            it.printStackTrace()
        }
    }

    override fun setTargetSeverity(targetSeverity: Severity) {
        Configurator.setAllLevels(LogManager.getRootLogger().name, targetSeverity.toLevel())
    }

    companion object {

        const val FILENAME = "app.log"

        fun Severity.toLevel(): Level {
            return when (this) {
                Severity.DISABLED -> Level.OFF
                Severity.ERROR -> Level.ERROR
                Severity.WARNING -> Level.WARN
                Severity.INFO -> Level.INFO
                Severity.DEBUG -> Level.DEBUG
                Severity.VERBOSE -> Level.TRACE
            }
        }
    }
}
