@file:OptIn(DelicateCoroutinesApi::class)

package com.cramsan.framework.sample.jvm

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.framework.sample.jvm.assertions.AssertScreen
import com.cramsan.framework.sample.jvm.eventlogger.EventLoggerScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.core.context.startKoin

/**
 * Application entry point.
 */
fun main() = application {
    // Start Koin with moduleA & moduleB
    startKoin {
        modules(FrameworkModule, ViewModelModule)
    }

    FrameworkInitializer().initialize()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            Screen()
        }
    }
}

enum class LibraryDestinations(val destination: String, val enabled: Boolean = false) {
    ASSERT("assert", true),
    BUILD("build"),
    CORE("core"),
    CORE_COMPOSE("corecompose"),
    CRASHHANDLER("crashhandler"),
    HALT("halt"),
    INTERFACELIB("interfacelib"),
    INTERFACELIB_TEST("interfacelibtest"),
    LOGGING("logging", true),
    METRICS("metrics"),
    PREFERENCES("preferences"),
    REMOTECONFIG("remoteconfig"),
    TEST("test"),
    THREAD("thread"),
    USEREVENTS("userevents"),
    UTILS("utils"),
}

@Composable
fun Screen() {
    var selectedTab: LibraryDestinations? by remember { mutableStateOf(null) }

    Column {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            if (selectedTab != null) {
                Button(onClick = { selectedTab = null }) {
                    Text(text = "Back")
                }
                Spacer(Modifier.size(20.dp))
            }

            LibraryDestinations.values().forEach {
                if (it.enabled) {
                    Button(onClick = { selectedTab = it }) {
                        Text(text = it.destination)
                    }
                }
            }
        }

        when (selectedTab) {
            LibraryDestinations.ASSERT -> {
                AssertScreen()
            }
            LibraryDestinations.BUILD -> TODO()
            LibraryDestinations.CORE -> TODO()
            LibraryDestinations.CORE_COMPOSE -> TODO()
            LibraryDestinations.CRASHHANDLER -> TODO()
            LibraryDestinations.HALT -> TODO()
            LibraryDestinations.INTERFACELIB -> TODO()
            LibraryDestinations.INTERFACELIB_TEST -> TODO()
            LibraryDestinations.LOGGING -> {
                EventLoggerScreen()
            }
            LibraryDestinations.METRICS -> TODO()
            LibraryDestinations.PREFERENCES -> TODO()
            LibraryDestinations.REMOTECONFIG -> TODO()
            LibraryDestinations.TEST -> TODO()
            LibraryDestinations.THREAD -> TODO()
            LibraryDestinations.USEREVENTS -> TODO()
            LibraryDestinations.UTILS -> TODO()
            null -> {}
        }
    }
}
