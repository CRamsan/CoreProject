package com.cramsan.ps2link.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.cramsan.ps2link.ui.R

@Composable
internal actual fun PlaceholderPainter(): Painter {
    return painterResource(R.drawable.image_not_found)
}

@Composable
internal actual fun AsyncPainter(imageUrl: String): Painter {
    return rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = imageUrl).apply {
                crossfade(true)
                placeholder(R.drawable.image_not_found)
                error(R.drawable.image_not_found)
                scale(Scale.FIT)
            }.build(),
    )
}
