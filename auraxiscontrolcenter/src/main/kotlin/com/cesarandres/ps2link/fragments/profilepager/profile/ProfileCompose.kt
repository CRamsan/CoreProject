package com.cesarandres.ps2link.fragments.profilepager.profile

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toStringResource
import com.cramsan.ps2link.ui.widgets.BR
import com.cramsan.ps2link.ui.widgets.BRBar
import com.cramsan.ps2link.ui.widgets.Cert
import com.cramsan.ps2link.ui.widgets.CertBar
import com.cramsan.ps2link.ui.widgets.FactionIcon
import kotlinx.datetime.Instant
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@OptIn(ExperimentalTime::class)
@Composable
fun ProfileCompose(
    faction: Faction?,
    br: Int?,
    percentToNextBR: Float?,
    certs: Int?,
    percentToNextCert: Float?,
    loginStatus: LoginStatus?,
    lastLogin: Instant?,
    outfit: Outfit?,
    server: String?,
    timePlayed: Duration?,
    isLoading: Boolean,
    prettyTime: PrettyTime,
    eventHandler: ProfileEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.padding(Padding.medium)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Faction icon
                FactionIcon(
                    modifier = Modifier.size(Size.xxlarge),
                    faction = faction ?: Faction.UNKNOWN
                )

                val mediumModifier = Modifier.fillMaxWidth().padding(Padding.medium)
                val smallModifier = Modifier.fillMaxWidth().padding(Padding.small)

                // BR Progress bar
                FrameSlim(modifier = mediumModifier) {
                    Row(modifier = smallModifier, verticalAlignment = Alignment.CenterVertically) {
                        BR(level = br ?: 0)
                        BRBar(
                            modifier = Modifier.weight(1f),
                            percentageToNextLevel = percentToNextBR ?: 0f
                        )
                        BR(level = (br ?: 0) + 1, enabled = false)
                    }
                }

                // Next cert progress bar
                FrameSlim(modifier = mediumModifier) {
                    Row(modifier = smallModifier, verticalAlignment = Alignment.CenterVertically) {
                        CertBar(
                            modifier = Modifier.weight(1f),
                            percentageToNextCert = percentToNextCert ?: 0f
                        )
                        Cert(certs ?: 0)
                    }
                }

                // Character base stats
                FrameSlim(modifier = mediumModifier) {
                    Column(modifier = smallModifier) {
                        // Login status
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(R.string.text_status))
                                Text(
                                    text = (loginStatus ?: LoginStatus.UNKNOWN).toStringResource(),
                                    color = loginStatus.toColor()
                                )
                            }
                        }

                        // Last login
                        val lastLoginString = lastLogin?.let {
                            prettyTime.format(Date(it.toEpochMilliseconds()))
                        } ?: stringResource(R.string.text_unknown)
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(R.string.text_last_login))
                                Text(text = lastLoginString)
                            }
                        }

                        // Outfit
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(R.string.title_outfit))
                                SlimButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = outfit != null,
                                    onClick = {
                                        outfit?.let {
                                            eventHandler.onOutfitSelected(it.id, it.namespace)
                                        }
                                    }
                                ) {
                                    Text(text = outfit?.name ?: stringResource(R.string.text_none))
                                }
                            }
                        }

                        // Server
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(R.string.title_server))
                                Text(text = server ?: stringResource(R.string.text_unknown))
                            }
                        }

                        // Hours played
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(R.string.text_hours_played))
                                Text(
                                    text = timePlayed?.inHours?.roundToInt()?.toString() ?: stringResource(R.string.text_unknown)
                                )
                            }
                        }
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@MainThread
interface ProfileEventHandler {
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun Preview() {
    ProfileCompose(
        faction = Faction.VS,
        br = 80,
        percentToNextBR = 75f,
        certs = 1000,
        percentToNextCert = 50f,
        loginStatus = LoginStatus.ONLINE,
        lastLogin = Instant.DISTANT_FUTURE,
        outfit = null,
        server = "Genudine",
        timePlayed = 1000.minutes,
        prettyTime = PrettyTime(),
        isLoading = true,
        eventHandler = object : ProfileEventHandler {
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
        },
    )
}
