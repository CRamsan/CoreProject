package com.cramsan.framework.sample.android.assert

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cramsan.framework.sample.android.theme.CoreProjectTheme

@Composable
fun AssertScreen(
    navController: NavController? = null,
) {
    val assertViewModel: AssertViewModel = viewModel()

    AssertScreenContent(navController, assertViewModel)
}

@Composable
private fun AssertScreenContent(
    navController: NavController? = null,
    eventHandler: AssertScreenEventHandler,
) {
    LazyColumn {
        item {
            Button(onClick = { eventHandler.tryAssert() }) {
                Text(text = "Try Assert")
            }
            Button(onClick = { eventHandler.tryAssertFalse() }) {
                Text(text = "Try tryAssertFalse")
            }
            Button(onClick = { eventHandler.tryAssertNull() }) {
                Text(text = "Try tryAssertNull")
            }
            Button(onClick = { eventHandler.tryAssertNotNull() }) {
                Text(text = "Try tryAssertNotNull")
            }
            Button(onClick = { eventHandler.tryAssertFailure() }) {
                Text(text = "Try tryAssertFailure")
            }
        }
    }
}

interface AssertScreenEventHandler {
    fun tryAssert()
    fun tryAssertFalse()
    fun tryAssertNull()
    fun tryAssertNotNull()
    fun tryAssertFailure()
}

@Preview
@Composable
fun AssertScreenPreview() {
    CoreProjectTheme {
        AssertScreenContent(
            eventHandler = object : AssertScreenEventHandler {
                override fun tryAssert() = Unit
                override fun tryAssertFalse() = Unit
                override fun tryAssertNull() = Unit
                override fun tryAssertNotNull() = Unit
                override fun tryAssertFailure() = Unit
            }
        )
    }
}
