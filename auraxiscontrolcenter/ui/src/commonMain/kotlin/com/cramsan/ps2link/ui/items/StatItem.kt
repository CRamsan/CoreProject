package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    label: String,
    allTime: String,
    today: String,
    thisWeek: String,
    thisMonth: String,
) {
    SlimButton(
        modifier = modifier,
    ) {
        Column {
            Text(label)

            val statModifier = remember { Modifier.weight(1f).padding(vertical = Padding.small) }
            val style = MaterialTheme.typography.caption

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = statModifier,
                    style = style,
                    text = allTime,
                )
                Text(
                    modifier = statModifier,
                    style = style,
                    text = today,
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = statModifier,
                    style = style,
                    text = thisWeek,
                )
                Text(
                    modifier = statModifier,
                    style = style,
                    text = thisMonth,
                )
            }
        }
    }
}