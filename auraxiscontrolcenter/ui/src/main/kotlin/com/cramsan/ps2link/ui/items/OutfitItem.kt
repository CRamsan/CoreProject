package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.NamespaceIcon

@Composable
fun OutfitItem(
    modifier: Modifier = Modifier,
    tag: String,
    name: String,
    memberCount: Int,
    namespace: Namespace,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1,
                )
                Row {
                    Text(tag, modifier = Modifier.weight(1f))
                    val args = memberCount.toString()
                    Text(stringResource(R.string.text_outfit_members, args), modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.width(Padding.medium))
            NamespaceIcon(modifier = Modifier.size(Size.xlarge), namespace = namespace)
        }
    }
}

@Preview
@Composable
fun OutfitItemPreview() {
    PS2Theme {
        OutfitItem(
            tag = "D3RP",
            name = "Derp Company",
            memberCount = 200,
            namespace = Namespace.PS2PC,
        )
    }
}
