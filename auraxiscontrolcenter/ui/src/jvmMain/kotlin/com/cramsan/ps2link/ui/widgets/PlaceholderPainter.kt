package com.cramsan.ps2link.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource

@Composable
internal actual fun PlaceholderPainter(): Painter {
    return painterResource("image_not_found.webp")
}

@Suppress("InjectDispatcher")
@Composable
internal actual fun AsyncPainter(imageUrl: String): Painter {
    val resource = asyncPainterResource(data = imageUrl)
    return when (resource) {
        is Resource.Failure -> PlaceholderPainter()
        is Resource.Loading -> PlaceholderPainter()
        is Resource.Success -> resource.value
    }
}
