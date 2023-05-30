package com.cesarandres.ps2link.fragments.profilepager.profile

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.SwipeToRefreshColumn
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.toText
import com.cramsan.ps2link.ui.widgets.BR
import com.cramsan.ps2link.ui.widgets.BRBar
import com.cramsan.ps2link.ui.widgets.Cert
import com.cramsan.ps2link.ui.widgets.CertBar
import com.cramsan.ps2link.ui.widgets.FactionIcon
import com.cramsan.ps2link.ui.widgets.NetworkImage
import kotlinx.datetime.Instant
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

/**
 * Render the Profile page with the provided arguments.
 */
@Suppress("ComplexMethod")
@OptIn(ExperimentalTime::class)
@Composable
fun ProfileCompose(
    faction: Faction?,
    br: Int?,
    prestige: Int?,
    percentToNextBR: Float?,
    certs: Int?,
    percentToNextCert: Float?,
    loginStatus: LoginStatus?,
    lastLogin: Instant?,
    creationTime: Instant?,
    outfit: Outfit?,
    server: String?,
    timePlayed: Duration?,
    prestigeIcon: String?,
    sessionCount: Long?,
    isLoading: Boolean,
    isError: Boolean,
    prettyTime: PrettyTime,
    eventHandler: ProfileEventHandler,
) {
    FrameBottom {
        SwipeToRefreshColumn(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Top Faction icon
                FactionIcon(
                    modifier = Modifier.size(Size.xxlarge),
                    faction = faction ?: Faction.UNKNOWN,
                )

                ErrorOverlay(
                    modifier = Modifier.wrapContentHeight(),
                    isError = isError,
                    error = stringResource(id = R.string.text_unkown_error)
                )

                val mediumModifier = Modifier.fillMaxWidth().padding(Padding.medium)
                val smallModifier = Modifier.fillMaxWidth().padding(Padding.small)

                // BR Progress bar
                FrameSlim(modifier = mediumModifier) {
                    Column {
                        Row(modifier = smallModifier, verticalAlignment = Alignment.CenterVertically) {
                            BR(level = br ?: 0)
                            BRBar(
                                modifier = Modifier.weight(1f),
                                percentageToNextLevel = percentToNextBR ?: 0f,
                            )
                            BR(level = (br ?: 0) + 1, enabled = false)
                        }

                        // Prestige Icon
                        if (prestigeIcon != null) {
                            NetworkImage(
                                modifier = Modifier
                                    .size(Size.xxlarge, Size.xxlarge)
                                    .align(Alignment.CenterHorizontally),
                                imageUrl = prestigeIcon,
                                contentScale = ContentScale.Fit,
                            )
                        }

                        // Prestige
                        if (prestige != null) {
                            FrameSlim(modifier = smallModifier.padding(horizontal = Padding.large)) {
                                Text(
                                    text = stringResource(com.cramsan.ps2link.ui.R.string.text_prestige, prestige),
                                    style = MaterialTheme.typography.caption,
                                )
                            }
                        }
                    }
                }

                // Next cert progress bar
                FrameSlim(modifier = mediumModifier) {
                    Row(modifier = smallModifier, verticalAlignment = Alignment.CenterVertically) {
                        CertBar(
                            modifier = Modifier.weight(1f),
                            percentageToNextCert = percentToNextCert ?: 0f,
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
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_status))
                                Text(
                                    text = (loginStatus ?: LoginStatus.UNKNOWN).toText(),
                                    color = loginStatus.toColor(),
                                )
                            }
                        }

                        // Last login
                        val lastLoginString = lastLogin?.let {
                            prettyTime.format(Date(it.toEpochMilliseconds()))
                        } ?: stringResource(com.cramsan.ps2link.ui.R.string.text_unknown)
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_last_login))
                                Text(text = lastLoginString)
                            }
                        }

                        // Outfit
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.title_outfit))
                                SlimButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = outfit != null,
                                    onClick = {
                                        outfit?.let {
                                            eventHandler.onOutfitSelected(it.id, it.namespace)
                                        }
                                    },
                                ) {
                                    Text(
                                        text = outfit?.name ?: stringResource(
                                            com.cramsan.ps2link.ui.R.string.text_none
                                        )
                                    )
                                }
                            }
                        }

                        // Server
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.title_server))
                                Text(text = server ?: stringResource(com.cramsan.ps2link.ui.R.string.text_unknown))
                            }
                        }

                        // Hours played
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_hours_played))
                                Text(
                                    text = timePlayed?.toDouble(DurationUnit.HOURS)?.roundToInt()
                                        ?.toString() ?: stringResource(com.cramsan.ps2link.ui.R.string.text_unknown),
                                )
                            }
                        }

                        // Account created
                        val accountCreatedString = creationTime?.let {
                            prettyTime.format(Date(it.toEpochMilliseconds()))
                        } ?: stringResource(com.cramsan.ps2link.ui.R.string.text_unknown)
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(
                                    text = stringResource(
                                        com.cramsan.ps2link.ui.R.string.text_member_since
                                    )
                                )
                                Text(text = accountCreatedString)
                            }
                        }

                        // Session count
                        FrameSlim(modifier = smallModifier) {
                            Column(modifier = smallModifier) {
                                Text(
                                    text = stringResource(
                                        com.cramsan.ps2link.ui.R.string.text_sessions_played
                                    )
                                )
                                Text(
                                    text = sessionCount?.toString() ?: stringResource(
                                        com.cramsan.ps2link.ui.R.string.text_unknown
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@MainThread
interface ProfileEventHandler {
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun Preview() {
    ProfileCompose(
        faction = Faction.VS,
        br = 80,
        prestige = 2,
        percentToNextBR = 75f,
        certs = 1000,
        percentToNextCert = 50f,
        loginStatus = LoginStatus.ONLINE,
        lastLogin = Instant.DISTANT_FUTURE,
        outfit = null,
        server = "Genudine",
        timePlayed = 1000.minutes,
        creationTime = Instant.DISTANT_PAST,
        sessionCount = 100,
        prestigeIcon = null,
        prettyTime = PrettyTime(),
        isLoading = true,
        isError = false,
        eventHandler = object : ProfileEventHandler {
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
