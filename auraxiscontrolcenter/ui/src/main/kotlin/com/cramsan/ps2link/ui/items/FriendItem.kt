package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toStringResource

@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    label: String,
    loginStatus: LoginStatus = LoginStatus.UNKNOWN,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = loginStatus.toStringResource(),
                color = loginStatus.toColor()
            )
        }
    }
}

@Preview
@Composable
fun FriendItemPreview() {
    PS2Theme {
        FriendItem(
            label = "Cramsan",
            loginStatus = LoginStatus.ONLINE,
        )
    }
}
