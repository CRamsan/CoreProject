package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toStringResource
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
        modifier = modifier
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
                        text = status.toStringResource(),
                        color = status.toColor(),
                        style = MaterialTheme.typography.body2,
                    )
                    Text(
                        modifier = Modifier.weight(2f),
                        text = population.toStringResource(),
                        color = population.toColor(),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
            }
            NamespaceIcon(modifier = Modifier.size(Size.xlarge), namespace = namespace)
        }
    }
}

@Preview
@Composable
fun ServerItemPreview() {
    PS2Theme {
        Column {
            ServerItem(
                serverName = "Connery",
                population = Population.HIGH,
                status = ServerStatus.ONLINE,
                namespace = Namespace.PS2PC,
            )
            ServerItem(
                serverName = "Soltech",
                population = Population.MEDIUM,
                status = ServerStatus.LOCKED,
                namespace = Namespace.PS2PC,
            )
            ServerItem(
                serverName = "Connery",
                population = Population.LOW,
                status = ServerStatus.OFFLINE,
                namespace = Namespace.PS2PC,
            )
            ServerItem(
                serverName = "Connery",
                population = Population.UNKNOWN,
                status = ServerStatus.UNKNOWN,
                namespace = Namespace.PS2PC,
            )
        }
    }
}
