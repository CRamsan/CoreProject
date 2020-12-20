package com.cramsan.ps2link.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.PetProjectTheme

@Composable
fun BoldButton(name: String, onClick: () -> Unit = {}) {
    val shape = CutCornerShape(topRight = 10.dp, bottomLeft = 10.dp)
    Text(
        text = "Hello $name!",
        modifier = Modifier.background(MaterialTheme.colors.primary)
            .clip(shape)
            .clickable(onClick = onClick)
    )
}

@Preview
@Composable
fun DefaultPreview() {
    val onClick = {}
    PetProjectTheme {
        BoldButton("Android", onClick)
    }
}
