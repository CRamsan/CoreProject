package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.undefined
import com.cramsan.ps2link.ui.toPainter

@Composable
fun NamespaceIcon(
    modifier: Modifier = Modifier,
    namespace: Namespace,
) {
    FrameSlim(
        modifier = Modifier
            .padding(Padding.medium)
            .background(color = undefined, shape = Shapes.medium),
    ) {
        // TODO: Add content description
        Image(
            modifier = modifier,
            painter = namespace.toPainter(),
            contentDescription = null,
        )
    }
}
