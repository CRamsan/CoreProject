package com.cramsan.ps2link.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.PS2Theme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(hint)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hideSoftwareKeyboard()
            }
        ),
        onValueChange = onValueChange
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun SearchFieldPreview() {
    PS2Theme {
        Column {
            SearchField(value = "cramsan", hint = "Player name", onValueChange = { /*TODO*/ })
        }
    }
}
