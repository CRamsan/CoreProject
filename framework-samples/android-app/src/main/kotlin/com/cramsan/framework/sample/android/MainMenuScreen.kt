package com.cramsan.framework.sample.android

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.cramsan.framework.sample.android.theme.CoreProjectTheme

@Composable
fun MainMenu(
    navController: NavController? = null,
) {
    MainMenuContent(navController)
}

@Composable
private fun MainMenuContent(
    navController: NavController? = null,
) {
    LazyColumn {
        items(LibraryDestinations.values()) {
            Button(onClick = { navController?.navigate(it.destination) }) {
                Text(text = it.name)
            }
        }
    }
}

enum class LibraryDestinations(val destination: String) {
    ASSERT("assert"),
    BUILD("build"),
    CORE("core"),
    CORE_COMPOSE("corecompose"),
    CRASHHANDLER("crashhandler"),
    HALT("halt"),
    INTERFACELIB("interfacelib"),
    INTERFACELIB_TEST("interfacelibtest"),
    LOGGING("logging"),
    METRICS("metrics"),
    PREFERENCES("preferences"),
    REMOTECONFIG("remoteconfig"),
    TEST("test"),
    THREAD("thread"),
    USEREVENTS("userevents"),
    UTILS("utils"),
}
@Preview
@Composable
fun MainMenuPreview() {
    CoreProjectTheme {
        MainMenu()
    }
}
