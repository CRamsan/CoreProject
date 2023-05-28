package com.cramsan.framework.sample.android.app.homepage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.framework.sample.android.app.theme.CoreProjectTheme

/**
 * Based on the official documentation for Scaffolding.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Suppress("DEPRECATION")
@Composable
fun HomePageScreen(
    loading: Boolean,
    title: String?,
    subtitle: String?,
    message: String?,
    refreshAction: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Column(
                // consume insets as scaffold doesn't do it by default
                modifier = Modifier
                    .fillMaxSize()
                    .consumedWindowInsets(innerPadding),
            ) {
                if (!loading) {
                    Text(
                        modifier = Modifier.clickable { refreshAction() },
                        text = title ?: "",
                    )
                    Text(
                        modifier = Modifier.clickable { refreshAction() },
                        text = subtitle ?: "",
                    )

                    Text(
                        modifier = Modifier.clickable { refreshAction() },
                        text = message ?: "",
                    )
                }
            }
        }
    )
}

/**
 *
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoreProjectTheme {
        HomePageScreen(
            loading = false,
            "Hello",
            "Title",
            "Text",
            { },
        )
    }
}
