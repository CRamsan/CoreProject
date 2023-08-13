@file:OptIn(DelicateCoroutinesApi::class)

package com.cramsan.framework.sample.jvm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.framework.sample.jvm.assert.AssertScreen
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

@Composable
fun Screen() {
    Row {
        Column {
            AssertScreen()
        }
    }
}
