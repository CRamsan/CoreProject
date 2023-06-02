package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toText

@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    label: String,
    loginStatus: LoginStatus = LoginStatus.UNKNOWN,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                modifier = Modifier.padding(start = Padding.small),
                text = loginStatus.toText(),
                color = loginStatus.toColor(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}