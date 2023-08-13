package com.cramsan.framework.sample.jvm.assert

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.koin.compose.koinInject

@Composable
fun AssertScreen(
    assertViewModel: AssertViewModel = koinInject(),
) {
    AssertScreenContent(assertViewModel)
}

@Composable
private fun AssertScreenContent(
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
    MaterialTheme {
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
