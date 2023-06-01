package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 *
 * Display an image that is loaded from the [imageUrl]. Provide a [contentDescription]
 * to ensure the image is readable by accessibility services. The [placeHolder] will be
 * displayed while the image is not loaded. Provide a [contentScale] to change how the
 * image fits within it's bounds.
 */
@Suppress("FunctionNaming")
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    contentDescription: String? = null,
    placeHolderPainter: Painter = PlaceholderPainter(),
    contentScale: ContentScale = ContentScale.Fit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {


        val painter = if (imageUrl == null) {
            placeHolderPainter
        } else {
            AsyncPainter(imageUrl)
        }
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painter,
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    }
}

@Composable
internal expect fun PlaceholderPainter(): Painter

@Composable
internal expect fun AsyncPainter(imageUrl: String): Painter