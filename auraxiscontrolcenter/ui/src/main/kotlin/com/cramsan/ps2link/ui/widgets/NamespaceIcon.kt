package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.undefined

@Composable
fun NamespaceIcon(
    modifier: Modifier = Modifier,
    namespace: Namespace,
) {
    val resourceId = when (namespace) {
        Namespace.PS2PC -> R.drawable.namespace_pc
        Namespace.PS2PS4US -> R.drawable.namespace_ps4us
        Namespace.PS2PS4EU -> R.drawable.namespace_ps4eu
        else -> {
            R.drawable.namespace_pc
        }
    }

    FrameSlim(
        modifier = Modifier
            .padding(Padding.medium)
            .background(color = undefined, shape = Shapes.medium)
    ) {
        // TODO: Add content description
        Image(
            modifier = modifier,
            painter = painterResource(id = resourceId),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun NamespaceIconPreview() {
    PS2Theme {
        Column {
            NamespaceIcon(namespace = Namespace.PS2PS4US)
            NamespaceIcon(namespace = Namespace.PS2PS4EU)
            NamespaceIcon(namespace = Namespace.PS2PC)
            NamespaceIcon(namespace = Namespace.UNDETERMINED)
        }
    }
}
