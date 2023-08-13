package com.cramsan.framework.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cramsan.framework.sample.android.assert.AssertScreen
import com.cramsan.framework.sample.android.theme.CoreProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            CoreProjectTheme {
                NavHost(navController = navController, startDestination = MAIN_MENU_DESTINATION) {
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
                        MainMenu(navController)
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
}

const val MAIN_MENU_DESTINATION = "main_menu"
