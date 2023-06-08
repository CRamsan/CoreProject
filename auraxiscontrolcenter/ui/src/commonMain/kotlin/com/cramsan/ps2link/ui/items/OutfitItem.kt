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
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.NamespaceIcon

@Composable
fun OutfitItem(
    modifier: Modifier = Modifier,
    tag: String,
    name: String,
    memberCount: String,
    namespace: Namespace,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier,
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
                    Text(
                        text = tag,
                        modifier = Modifier.weight(1f),
                    )
                    Text(memberCount, modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.width(Padding.medium))
            NamespaceIcon(modifier = Modifier.size(Size.xlarge), namespace = namespace)
        }
    }
}
