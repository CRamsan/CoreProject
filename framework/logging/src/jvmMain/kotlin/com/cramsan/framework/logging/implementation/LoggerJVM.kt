package com.cramsan.framework.logging.implementation

import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.Severity
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration

/**
 * Logger that prints to stdout.
 */
class LoggerJVM(private val logToFile: Boolean) : EventLoggerDelegate {

    override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val level = severity.toLevel()
        val logMessage = "[$tag]$message"
        logger.log(level, logMessage, throwable)
        throwable?.let {
            it.printStackTrace()
        }
    }

    override fun setTargetSeverity(targetSeverity: Severity) {
        buildLogger(targetSeverity.toLevel(), logToFile)
    }

    private fun buildLogger(level: Level, logToFile: Boolean) {
        val builder = ConfigurationBuilderFactory.newConfigurationBuilder()
        val rootLogger: RootLoggerComponentBuilder = builder.newRootLogger(level)

        builder.setStatusLevel(level)

        builder.setConfigurationName("DefaultRollingFileLogger")

        val appender = if (logToFile) {
            createFileAppender(builder)
        } else {
            createConsoleAppender(builder)
        }

        builder.add(appender)
        rootLogger.add(builder.newAppenderRef(appender.name))
        builder.add(rootLogger)
        Configurator.reconfigure(builder.build())
    }

    private fun createConsoleAppender(builder: ConfigurationBuilder<BuiltConfiguration>): AppenderComponentBuilder {
        // set the pattern layout and pattern
        val layoutBuilder: LayoutComponentBuilder = builder.newLayout("PatternLayout")
            .addAttribute("pattern", LOG_PATTERN)

        return builder.newAppender("Console", "CONSOLE")
            .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
            .add(layoutBuilder)
    }

    private fun createFileAppender(builder: ConfigurationBuilder<BuiltConfiguration>): AppenderComponentBuilder {
        // set the pattern layout and pattern
        val layoutBuilder: LayoutComponentBuilder = builder.newLayout("PatternLayout")
            .addAttribute("pattern", LOG_PATTERN)

        // specifying the policy for rolling file
        val triggeringPolicy = builder.newComponent("Policies")
            .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10MB"))

        // create a console appender
        return builder.newAppender("LogToRollingFile", "RollingFile")
            .addAttribute("fileName", FILENAME)
            .addAttribute("filePattern", "$FILENAME-%d{MM-dd-yy-HH-mm-ss}.log.")
            .add(layoutBuilder)
            .addComponent(triggeringPolicy)
    }

    companion object {

        const val LOG_PATTERN = "%d\t%p\t[%t]\t%m%n"

        const val FILENAME = "app.log"

        private var logger = LogManager.getLogger(Companion::class.java)

        private fun Severity.toLevel(): Level {
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
