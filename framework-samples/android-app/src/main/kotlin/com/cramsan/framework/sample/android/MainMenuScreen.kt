package com.cramsan.framework.sample.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.cramsan.framework.sample.android.theme.CoreProjectTheme

@Composable
fun MainMenu(
    navController: NavController? = null,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        MainMenuContent(navController)
    }
}

@Composable
private fun MainMenuContent(
    navController: NavController? = null,
) {
    val buttonModifier = Modifier
        .fillMaxWidth()

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
    ) {
        LibraryDestinations.values().forEach {
            if (it.enabled) {
                Button(
                    modifier = buttonModifier,
                    onClick = { navController?.navigate(it.destination) },
                ) {
                    Text(text = it.name)
                }
            }
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
@Preview
@Composable
fun MainMenuPreview() {
    CoreProjectTheme {
        MainMenu()
    }
}
