package com.cramsan.ps2link.network.ws.testgui.ui.lib.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.network.ws.testgui.ui.lib.SlimButton
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Padding
import com.cramsan.ps2link.network.ws.testgui.ui.lib.toColor
import com.cramsan.ps2link.network.ws.testgui.ui.lib.toStringResource

@Composable
fun OutfitMemberItem(
    modifier: Modifier = Modifier,
    label: String,
    outfitRank: String,
    loginStatus: LoginStatus,
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
                modifier = Modifier.padding(horizontal = Padding.small),
                text = outfitRank,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = loginStatus.toStringResource(),
                color = loginStatus.toColor(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}
