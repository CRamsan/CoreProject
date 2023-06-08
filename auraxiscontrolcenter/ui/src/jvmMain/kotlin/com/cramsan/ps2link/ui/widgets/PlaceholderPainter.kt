package com.cramsan.ps2link.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

@Composable
internal actual fun PlaceholderPainter(): Painter {
    return painterResource("image_not_found.webp")
}

@Composable
internal actual fun AsyncPainter(imageUrl: String): Painter {
    val image: ImageBitmap? by produceState<ImageBitmap?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                URL(imageUrl).openStream().buffered().use {
                    loadImageBitmap(it)
                }
            } catch (e: IOException) {
                null
            }
        }
    }

    return image?.let { BitmapPainter(it) } ?: PlaceholderPainter()
}
