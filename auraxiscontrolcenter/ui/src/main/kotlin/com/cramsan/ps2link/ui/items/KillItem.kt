package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.widgets.FactionIcon
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoilApi::class)
@Composable
fun KillItem(
    modifier: Modifier = Modifier,
    killType: KillType = KillType.UNKNOWN,
    faction: Faction = Faction.UNKNOWN,
    attacker: String? = null,
    time: Instant? = null,
    weaponName: String? = null,
    weaponImage: String? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.75f)) {
                Text(
                    text = stringResource(
                        when (killType) {
                            KillType.KILL -> R.string.text_killed_caps
                            KillType.KILLEDBY -> R.string.text_killed_by_caps
                            KillType.SUICIDE -> R.string.text_suicide_caps
                            KillType.UNKNOWN -> R.string.title_unkown
                        }
                    ),
                    color = killType.toColor(),
                )
                FactionIcon(modifier = Modifier.size(Size.xxlarge), faction = faction)
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = Padding.small)) {
                Text(attacker ?: stringResource(R.string.text_unknown))
                Spacer(modifier = Modifier.size(Padding.medium))
                Text(
                    text = time?.let {
                        formatter.format(Date(it.toEpochMilliseconds()))
                    } ?: stringResource(R.string.text_unknown),
                    style = MaterialTheme.typography.caption,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(weaponName ?: stringResource(R.string.text_unknown))
                val painter = rememberImagePainter(
                    data = weaponImage,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.image_not_found)
                    }
                )
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = null
                )
                when (painter.state) {
                    is ImagePainter.State.Loading, is ImagePainter.State.Error, ImagePainter.State.Empty -> {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.image_not_found),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    }
                    else -> Unit
                }
            }
        }
    }
}

var formatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())

@Preview
@Composable
fun ProfileKillPreview() {
    PS2Theme {
        KillItem(
            killType = KillType.KILL,
            faction = Faction.TR,
            attacker = "Cramsan",
            time = Instant.DISTANT_FUTURE,
            weaponName = "Masamune",
        )
    }
}
