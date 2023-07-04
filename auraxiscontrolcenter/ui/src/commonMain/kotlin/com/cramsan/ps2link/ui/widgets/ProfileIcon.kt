package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.ui.toPainter

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    characterClass: CharacterClass,
) {
    // TODO: Add content description
    Image(
        modifier = modifier,
        painter = characterClass.toPainter(),
        contentDescription = "$characterClass",
    )
}
