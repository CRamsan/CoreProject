package com.cramsan.framework.sample.jvm.eventlogger

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.framework.logging.Severity
import org.koin.compose.koinInject

@Composable
fun EventLoggerScreen() {
    val eventLoggerViewModel: EventLoggerViewModel = koinInject()

    EventLoggerScreenContent(eventLoggerViewModel)
}

@Composable
private fun EventLoggerScreenContent(
    eventHandler: EventLoggerScreenEventHandler,
) {
    Column {
        Text(text = "Set Severity")
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            Severity.values().forEach {
                Button(onClick = { eventHandler.setSeverity(it) }) {
                    Text(text = it.name)
                }
            }
        }
        Text(text = "Log a message")
        Button(onClick = { eventHandler.tryLogV() }) {
            Text(text = "Try LogV")
        }
        Button(onClick = { eventHandler.tryLogD() }) {
            Text(text = "Try LogD")
        }
        Button(onClick = { eventHandler.tryLogI() }) {
            Text(text = "Try LogI")
        }
        Button(onClick = { eventHandler.tryLogW() }) {
            Text(text = "Try LogW")
        }
        Button(onClick = { eventHandler.tryLogE() }) {
            Text(text = "Try LogE")
        }
        Button(onClick = { eventHandler.toggleLogToFile() }) {
            Text(text = "Toggle Log To File")
        }
    }
}

interface EventLoggerScreenEventHandler {
    fun tryLogV()
    fun tryLogD()
    fun tryLogI()
    fun tryLogW()
    fun tryLogE()
    fun toggleLogToFile()
    fun setSeverity(severity: Severity)
}

@Preview
@Composable
fun AssertScreenPreview() {
    MaterialTheme {
        EventLoggerScreenContent(
            eventHandler = object : EventLoggerScreenEventHandler {
                override fun tryLogV() = Unit
                override fun tryLogD() = Unit
                override fun tryLogI() = Unit
                override fun tryLogW() = Unit
                override fun tryLogE() = Unit
                override fun toggleLogToFile() = Unit
                override fun setSeverity(severity: Severity) = Unit
            }
        )
    }
}
