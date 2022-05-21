package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.theme.certBackground
import com.cramsan.ps2link.ui.theme.certOrange
import com.cramsan.ps2link.ui.theme.certWhite
import kotlin.math.max

@Composable
fun Cert(certs: Int) {
    // TODO: Add content description
    Image(
        modifier = Modifier.padding(horizontal = Padding.small),
        painter = painterResource(id = R.drawable.cert),
        contentDescription = null,
    )
    Text(
        text = certs.toString(),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun CertBar(
    modifier: Modifier = Modifier,
    percentageToNextCert: Float,
) {
    val height = Size.large
    Row(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .padding(Padding.xsmall)
            .clip(CutCornerShape(Size.xsmall))
            .background(BRGradient)
            .border(width = Size.micro, color = certBackground, shape = CutCornerShape(Size.xsmall)),
    ) {
        if (percentageToNextCert > 0) {
            Spacer(
                modifier = Modifier
                    .weight(percentageToNextCert),
            )
            val remaining = max(1f, 100 - percentageToNextCert)
            Box(
                modifier = Modifier
                    .weight(remaining)
                    .fillMaxHeight()
                    .background(certBackground),
            )
        }
    }
}

private val BRGradient = Brush.horizontalGradient(listOf(certWhite, certOrange))

@Preview(
    widthDp = 200,
    heightDp = 25,
)
@Composable
fun CertBarPreview() {
    PS2Theme {
        Row(modifier = Modifier.fillMaxWidth()) {
            CertBar(percentageToNextCert = 75f)
            Cert(2155)
        }
    }
}
