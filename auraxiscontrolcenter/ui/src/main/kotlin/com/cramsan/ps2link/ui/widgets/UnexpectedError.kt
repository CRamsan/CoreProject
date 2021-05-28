package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.theme.MaxDialogSize
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.errorColor

@Composable
fun UnexpectedError(
    modifier: Modifier = Modifier,
    message: String,
) {
    FrameSlim(modifier) {
        Text(
            text = message,
            modifier = Modifier.padding(Padding.medium),
            color = errorColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun UnexpectedError(
    modifier: Modifier = Modifier,
    resourceId: Int = R.string.text_unkown_error,
) {
    UnexpectedError(
        modifier = modifier.widthIn(max = MaxDialogSize),
        message = stringResource(id = resourceId)
    )
}

@Preview(
    widthDp = 300,
    heightDp = 350,
)
@Composable
fun UnexpectedErrorPreview() {
    PS2Theme {
        FrameBottom {
            UnexpectedError()
        }
    }
}
