package com.cramsan.ps2link.appfrontend.profilepager.profile

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
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Rank
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
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
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Render the Profile page with the provided arguments.
 */
@Suppress("ComplexMethod")
@Composable
fun ProfileCompose(
    modifier: Modifier = Modifier,
    faction: Faction?,
    br: Int?,
    prestige: Int?,
    percentToNextBR: Float?,
    certs: Int?,
    percentToNextCert: Float?,
    loginStatus: LoginStatus?,
    lastLogin: String?,
    creationTime: String?,
    outfit: Outfit?,
    server: String?,
    timePlayed: Duration?,
    prestigeIcon: String?,
    sessionCount: Long?,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: ProfileEventHandler,
) {
    FrameBottom(
        modifier = modifier,
    ) {
        SwipeToRefreshColumn(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            // Top Faction icon
            FactionIcon(
                modifier = Modifier.size(Size.xxlarge),
                faction = faction ?: Faction.UNKNOWN,
            )

            ErrorOverlay(
                modifier = Modifier.wrapContentHeight(),
                isError = isError,
                error = UnknownErrorText()
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
                                text = TextPrestige(prestige),
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
                            Text(text = TextStatus())
                            Text(
                                text = (loginStatus ?: LoginStatus.UNKNOWN).toText(),
                                color = loginStatus.toColor(),
                            )
                        }
                    }

                    // Last login
                    val lastLoginString = lastLogin ?: UnknownText()
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = TextLastLogin())
                            Text(text = lastLoginString)
                        }
                    }

                    // Outfit
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = TextOutfit())
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
                                    text = outfit?.name ?: TextNone()
                                )
                            }
                        }
                    }

                    // Server
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = TextServer())
                            Text(text = server ?: UnknownText())
                        }
                    }

                    // Hours played
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(text = TextHoursPlayed())
                            Text(
                                text = timePlayed?.toDouble(DurationUnit.HOURS)?.roundToInt()
                                    ?.toString() ?: UnknownText(),
                            )
                        }
                    }

                    // Account created
                    val accountCreatedString = creationTime ?: UnknownText()
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(
                                text = TextMembersSince()
                            )
                            Text(text = accountCreatedString)
                        }
                    }

                    // Session count
                    FrameSlim(modifier = smallModifier) {
                        Column(modifier = smallModifier) {
                            Text(
                                text = TextSessionsPlayer()
                            )
                            Text(
                                text = sessionCount?.toString() ?: UnknownText()
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 *
 */
interface ProfileEventHandler {
    /**
     *
     */
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}

@Composable
expect fun TextPrestige(prestige: Int): String

@Composable
expect fun TextStatus(): String

@Composable
expect fun TextLastLogin(): String

@Composable
expect fun TextOutfit(): String

@Composable
expect fun TextNone(): String

@Composable
expect fun TextServer(): String

@Composable
expect fun TextHoursPlayed(): String

@Composable
expect fun TextMembersSince(): String

@Composable
expect fun TextSessionsPlayer(): String

/**
 *
 */
data class CharacterUIModel(
    val characterId: String,
    val name: String? = null,
    val activeProfileId: CharacterClass = CharacterClass.UNKNOWN,
    val loginStatus: LoginStatus = LoginStatus.UNKNOWN,
    val certs: Long? = null,
    val battleRank: Long? = null,
    val prestige: Long?,
    val percentageToNextCert: Double? = null,
    val percentageToNextBattleRank: Double? = null,
    val creationTime: String?,
    val sessionCount: Long?,
    val lastLogin: String? = null,
    val timePlayed: Duration? = null,
    val faction: Faction = Faction.UNKNOWN,
    val server: Server? = null,
    val outfit: Outfit? = null,
    val outfitRank: Rank? = null,
    val namespace: Namespace,
    val lastUpdate: String? = null,
    val cached: Boolean,
)
