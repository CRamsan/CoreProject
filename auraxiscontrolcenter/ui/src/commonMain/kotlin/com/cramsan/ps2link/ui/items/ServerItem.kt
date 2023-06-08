package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toText
import com.cramsan.ps2link.ui.widgets.NamespaceIcon

@Composable
fun ServerItem(
    modifier: Modifier = Modifier,
    serverName: String,
    population: Population,
    status: ServerStatus,
    namespace: Namespace,
) {
    SlimButton(
        modifier = modifier,
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = serverName,
                    style = MaterialTheme.typography.body1,
                )
                Row {
                    Text(
                        modifier = Modifier.weight(2f),
                        text = status.toText(),
                        color = status.toColor(),
                        style = MaterialTheme.typography.body2,
                    )
                    Text(
                        modifier = Modifier.weight(2f),
                        text = population.toText(),
                        color = population.toColor(),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
            }
            NamespaceIcon(
                modifier = Modifier.size(Size.xlarge).align(Alignment.CenterVertically),
                namespace = namespace,
            )
        }
    }
}
