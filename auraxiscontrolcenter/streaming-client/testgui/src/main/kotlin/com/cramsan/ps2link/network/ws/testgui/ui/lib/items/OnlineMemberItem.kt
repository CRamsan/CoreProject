package com.cramsan.ps2link.network.ws.testgui.ui.lib.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.network.ws.testgui.ui.lib.SlimButton
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Size
import com.cramsan.ps2link.network.ws.testgui.ui.lib.toImageRes

@Composable
fun OnlineMemberItem(
    modifier: Modifier = Modifier,
    label: String,
    characterClass: CharacterClass,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(label)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.size(Size.large),
                painter = painterResource(characterClass.toImageRes()),
                contentDescription = "",
            )
        }
    }
}
