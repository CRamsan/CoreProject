package com.cesarandres.ps2link.fragments.outfitpager.outfit

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.cramsan.ps2link.ui.toStringResource
import com.cramsan.ps2link.ui.widgets.BR
import com.cramsan.ps2link.ui.widgets.BRBar
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
fun OutfitCompose(
    faction: Faction?,
    br: Int?,
    percentToNextBR: Int?,
    certs: Int?,
    percentToNextCert: Int?,
    loginStatus: LoginStatus?,
    lastLogin: String?,
    outfit: Outfit?,
    server: String?,
    hoursPlayed: Double?,
    isLoading: Boolean,
    eventHandler: OutfitEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Box {
            Column {
                // Top Faction icon
                FactionIcon(faction = faction ?: Faction.UNKNOWN)

                // BR Progress bar
                FrameSlim {
                    Row {
                        BR(level = br ?: 0)
                        BRBar(percentageToNextLevel = percentToNextBR ?: 0)
                        BR(level = (br ?: 0) + 1)
                    }
                }

                // Next cert progress bar
                FrameSlim {
                }

                // Character base stats
                FrameSlim {
                    Column {
                        // Login status
                        FrameSlim {
                            Column {
                                Text(text = stringResource(R.string.text_status))
                                Text(text = (loginStatus ?: LoginStatus.UNKNOWN).toStringResource())
                            }
                        }

                        // Last login
                        FrameSlim {
                            Column {
                                Text(text = stringResource(R.string.text_last_login))
                                Text(text = lastLogin ?: stringResource(R.string.text_unknown))
                            }
                        }

                        // Outfit
                        FrameSlim {
                            Column {
                                Text(text = stringResource(R.string.title_outfit))
                                SlimButton(
                                    enabled = outfit != null,
                                    onClick = {
                                        outfit?.let {
                                            eventHandler.onProfileSelected(it.id, it.namespace)
                                        }
                                    }
                                ) {
                                    Text(text = outfit?.name ?: stringResource(R.string.text_none))
                                }
                            }
                        }

                        // Server
                        FrameSlim {
                            Column {
                                Text(text = stringResource(R.string.title_server))
                                Text(text = server ?: stringResource(R.string.text_unknown))
                            }
                        }

                        // Hours played
                        FrameSlim {
                            Column {
                                Text(text = stringResource(R.string.text_hours_played))
                                Text(
                                    text = if (hoursPlayed == null) {
                                        hoursPlayed.toString()
                                    } else {
                                        stringResource(R.string.text_unknown)
                                    }
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
interface OutfitEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    OutfitCompose(
        faction = Faction.VS,
        br = 80,
        percentToNextBR = 75,
        certs = 1000,
        percentToNextCert = 50,
        loginStatus = LoginStatus.ONLINE,
        lastLogin = "Two hours ago",
        outfit = null,
        server = "Genudine",
        hoursPlayed = 1000.toDouble(),
        isLoading = true,
        eventHandler = object : OutfitEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
