package com.cramsan.ps2link.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import com.cramsan.framework.logging.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

@Composable
internal actual fun PlaceholderPainter(): Painter {
    return painterResource("image_not_found.webp")
}

@Suppress("InjectDispatcher")
@Composable
internal actual fun AsyncPainter(imageUrl: String): Painter {
    val image: ImageBitmap? by produceState<ImageBitmap?>(null) {
        // TODO: We should not be hardcoding the dispatcher. This approach is temporary only until
        // we find a permanent solution for doing async image loading.
        value = withContext(Dispatchers.IO) {
            try {
                URL(imageUrl).openStream().buffered().use {
                    loadImageBitmap(it)
                }
            } catch (e: IOException) {
                logE("AsyncPainter", "Error fetching image from: $imageUrl", e)
                null
            }
        }
    }

    return image?.let { BitmapPainter(it) } ?: PlaceholderPainter()
}
