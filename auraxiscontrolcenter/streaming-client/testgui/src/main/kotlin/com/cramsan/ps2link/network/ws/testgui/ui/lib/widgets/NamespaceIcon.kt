package com.cramsan.ps2link.network.ws.testgui.ui.lib.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.ui.lib.FrameSlim
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Padding
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Shapes
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.undefined

@Composable
fun NamespaceIcon(
    modifier: Modifier = Modifier,
    namespace: Namespace,
) {
    val resourceId = when (namespace) {
        Namespace.PS2PC -> "namespace_pc.webp"
        Namespace.PS2PS4US -> "namespace_ps4us.webp"
        Namespace.PS2PS4EU -> "namespace_ps4eu.webp"
        else -> {
            "namespace_pc.webp"
        }
    }

    FrameSlim(
        modifier = Modifier
            .padding(Padding.medium)
            .background(color = undefined, shape = Shapes.medium),
    ) {
        // TODO: Add content description
        Image(
            modifier = modifier,
            painter = painterResource(resourceId),
            contentDescription = null,
        )
    }
}
