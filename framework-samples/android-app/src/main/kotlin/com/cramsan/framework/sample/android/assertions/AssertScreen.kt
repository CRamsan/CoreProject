package com.cramsan.framework.sample.android.assertions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cramsan.framework.sample.android.theme.CoreProjectTheme

@Composable
fun AssertScreen(
    navController: NavController? = null,
) {
    val assertViewModel: AssertViewModel = hiltViewModel()

    AssertScreenContent(navController, assertViewModel)
}

@Composable
private fun AssertScreenContent(
    navController: NavController? = null,
    eventHandler: AssertScreenEventHandler,
) {
    val buttonModifier = Modifier.fillMaxWidth()
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
    ) {
        Button(modifier = buttonModifier, onClick = { eventHandler.tryAssert() }) {
            Text(text = "Try Assert")
        }
        Button(modifier = buttonModifier, onClick = { eventHandler.tryAssertFalse() }) {
            Text(text = "Try tryAssertFalse")
        }
        Button(modifier = buttonModifier, onClick = { eventHandler.tryAssertNull() }) {
            Text(text = "Try tryAssertNull")
        }
        Button(modifier = buttonModifier, onClick = { eventHandler.tryAssertNotNull() }) {
            Text(text = "Try tryAssertNotNull")
        }
        Button(modifier = buttonModifier, onClick = { eventHandler.tryAssertFailure() }) {
            Text(text = "Try tryAssertFailure")
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
