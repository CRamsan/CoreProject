package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.cramsan.ps2link.ui.R

/**
 *
 * Display an image that is loaded from the [imageUrl]. Provide a [contentDescription]
 * to ensure the image is readable by accessibility services. The [placeHolder] will be
 * displayed while the image is not loaded. Provide a [contentScale] to change how the
 * image fits within it's bounds.
 */
@Suppress("FunctionNaming")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    contentDescription: String? = null,
    placeHolder: Int = R.drawable.image_not_found,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val painter = if (imageUrl == null) {
            painterResource(placeHolder)
        } else {
            rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(placeHolder)
                    error(placeHolder)
                    scale(Scale.FIT)
                },
            )
        }
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painter,
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    }
}
