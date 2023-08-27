package com.cramsan.framework.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cramsan.framework.sample.android.assertions.AssertScreen
import com.cramsan.framework.sample.android.eventlogger.EventLoggerScreen
import com.cramsan.framework.sample.android.theme.CoreProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNavigation() {
    val navController = rememberNavController()

    CoreProjectTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Top App Bar") },
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MAIN_MENU_DESTINATION,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MAIN_MENU_DESTINATION) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.ASSERT.destination) {
                    AssertScreen(navController)
                }
                composable(LibraryDestinations.BUILD.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.CORE_COMPOSE.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.CORE.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.HALT.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.CRASHHANDLER.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.INTERFACELIB.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.LOGGING.destination) {
                    EventLoggerScreen(navController)
                }
                composable(LibraryDestinations.METRICS.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.PREFERENCES.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.REMOTECONFIG.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.THREAD.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.USEREVENTS.destination) {
                    MainMenu(navController)
                }
                composable(LibraryDestinations.UTILS.destination) {
                    MainMenu(navController)
                }
            }
        }
    }
}

const val MAIN_MENU_DESTINATION = "main_menu"
