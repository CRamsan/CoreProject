package com.cramsan.ps2link.network.ws.testgui.ui.screens.connection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.ui.BoldButton
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameCenter
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.SearchField
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.items.KillItem
import com.cramsan.ps2link.ui.items.ProfileItem
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.toColor
import com.cramsan.ps2link.ui.widgets.BR
import com.cramsan.ps2link.ui.widgets.BRBar
import com.cramsan.ps2link.ui.widgets.Cert
import com.cramsan.ps2link.ui.widgets.CertBar
import com.cramsan.ps2link.ui.widgets.FactionIcon
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseScreen
import com.cramsan.ps2link.network.ws.testgui.ui.theme.ScreenSizes
import com.cramsan.ps2link.ui.toText
import com.cramsan.ps2link.ui.unknownString
import kotlinx.collections.immutable.ImmutableList
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

/**
 * Main screen that is first displayed to the user.
 */
class MainScreen(
    private val applicationManager: ApplicationManager,
    private val serviceClient: DBGServiceClient,
    private val dispatcherProvider: DispatcherProvider,
    private val prettyTime: PrettyTime,
) : BaseScreen<MainViewModel>() {
    override fun createViewModel(): MainViewModel {
        return MainViewModel(applicationManager, serviceClient, dispatcherProvider)
    }

    @Composable
    override fun ScreenContent(viewModel: MainViewModel) {
        MainScreenContent(viewModel, prettyTime)
    }
}

@Composable
private fun MainScreenContent(
    viewModel: MainViewModel,
    prettyTime: PrettyTime,
) {
    val uiState by viewModel.uiState.collectAsState()

    Row {
        AnimatedVisibility(
            visible = !uiState.isRunning,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally(),
        ) {
            FrameSlim(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .widthIn(ScreenSizes.maxColumnWidth / 2)
                    .fillMaxHeight(),
            ) {
                SearchFragment(
                    uiState.characterName,
                    uiState.playerSuggestions,
                    uiState.isListLoading,
                    uiState.isError,
                    viewModel,
                )
            }
        }
        FrameSlim(
            modifier = Modifier
                .animateContentSize()
                .width(IntrinsicSize.Max)
                .widthIn(ScreenSizes.maxColumnWidth)
                .fillMaxHeight(),
        ) {
            CharacterFragment(
                uiState.selectedPlayer,
                uiState.actionLabel,
                uiState.isLoading,
                uiState.isError,
                viewModel,
                prettyTime,
            )
        }
        FrameSlim(
            modifier = Modifier
                .animateContentSize()
                .fillMaxSize(),
        ) {
            KillFeedFragment(
                uiState.killList,
                onEventSelected = { viewModel.onKillEventSelected(it) },
            )
        }
    }
}

@Composable
private fun SearchFragment(
    characterName: String,
    playerSuggestions: ImmutableList<Character>,
    isLoading: Boolean,
    isError: Boolean,
    viewModel: MainViewModel,
) {
    FrameSlim(
        modifier = Modifier.fillMaxSize().padding(Padding.medium),
    ) {
        Column {
            SearchField(
                modifier = Modifier.fillMaxWidth(),
                value = characterName,
                hint = "Enter a player name",
            ) { text ->
                viewModel.updateCharacterName(text)
            }
            Box {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    playerSuggestions.forEach {
                        ProfileItem(
                            modifier = Modifier.padding(vertical = Padding.micro),
                            label = it.name ?: "",
                            level = it.battleRank?.toInt() ?: 0,
                            faction = it.faction,
                            namespace = it.namespace,
                            onClick = { viewModel.selectCharacter(it) },
                        )
                    }
                }
                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(isError = isError, error = unknownString())
            }
        }
    }
}

@Suppress("LongMethod", "ComplexMethod")
@Composable
private fun CharacterFragment(
    selectedPlayer: Character?,
    actionLabel: String,
    isLoading: Boolean,
    isError: Boolean,
    viewModel: MainViewModel,
    prettyTime: PrettyTime,
) {
    Column(
        modifier = Modifier.padding(Padding.medium),
    ) {
        if (selectedPlayer != null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                FrameCenter {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                selectedPlayer.name ?: "UNKNOWN",
                                style = MaterialTheme.typography.h1,
                            )
                            FactionIcon(
                                modifier = Modifier.size(Size.xxlarge),
                                faction = selectedPlayer.faction,
                            )
                        }

                        val mediumModifier = Modifier.fillMaxWidth().padding(Padding.medium)
                        val smallModifier = Modifier.fillMaxWidth().padding(Padding.small)

                        // BR Progress bar
                        FrameSlim(modifier = mediumModifier) {
                            Column {
                                Row(modifier = smallModifier, verticalAlignment = Alignment.CenterVertically) {
                                    BR(level = selectedPlayer.battleRank?.toInt() ?: 0)
                                    BRBar(
                                        modifier = Modifier.weight(1f),
                                        percentageToNextLevel =
                                        selectedPlayer.percentageToNextBattleRank?.toFloat() ?: 0f,
                                    )
                                    BR(level = (selectedPlayer.battleRank?.toInt() ?: 0) + 1, enabled = false)
                                }

                                // Prestige
                                if (selectedPlayer.prestige != null) {
                                    FrameSlim(modifier = smallModifier.padding(horizontal = Padding.large)) {
                                        Text(
                                            text = "Prestige: ${selectedPlayer.prestige}",
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
                                    percentageToNextCert = (selectedPlayer.percentageToNextCert)?.toFloat() ?: 0f,
                                )
                                Cert((selectedPlayer.certs)?.toInt() ?: 0)
                            }
                        }

                        // Character base stats
                        FrameSlim(modifier = mediumModifier) {
                            Column(modifier = smallModifier) {
                                // Login status
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "STATUS:")
                                        Text(
                                            text = selectedPlayer.loginStatus.toText(),
                                            color = selectedPlayer.loginStatus.toColor(),
                                        )
                                    }
                                }

                                // Last login
                                val lastLoginString = selectedPlayer.lastLogin?.let {
                                    prettyTime.format(Date(it.toEpochMilliseconds()))
                                } ?: "UNKNOWN"
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "LAST LOGIN:")
                                        Text(text = lastLoginString)
                                    }
                                }

                                // Outfit
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "OUTFIT")
                                        SlimButton(
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = selectedPlayer.outfit != null,
                                        ) {
                                            Text(text = selectedPlayer.outfit?.name ?: "UNKNOWN")
                                        }
                                    }
                                }

                                // Server
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "SERVER:")
                                        Text(text = selectedPlayer.server?.serverName ?: "UNKNOWN")
                                    }
                                }

                                // Hours played
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "HOURS PLAYED:")
                                        Text(
                                            text = selectedPlayer.timePlayed?.toDouble(DurationUnit.HOURS)?.roundToInt()
                                                ?.toString() ?: "UNKNOWN",
                                        )
                                    }
                                }

                                // Account created
                                val accountCreatedString = selectedPlayer.creationTime?.let {
                                    prettyTime.format(Date(it.toEpochMilliseconds()))
                                } ?: "UNKNOWN"
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "PLAYER SINCE:")
                                        Text(text = accountCreatedString)
                                    }
                                }

                                // Session count
                                FrameSlim(modifier = smallModifier) {
                                    Column(modifier = smallModifier) {
                                        Text(text = "SESSIONS PLAYED:")
                                        Text(text = selectedPlayer.sessionCount?.toString() ?: "UNKNOWN")
                                    }
                                }
                            }
                        }
                    }
                }
                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(isError = isError, error = unknownString())
            }
        }
        FrameBottom(
            modifier = Modifier
                .wrapContentHeight(),
        ) {
            Row(
                modifier = Modifier.padding(Padding.medium),
            ) {
                SlimButton(
                    modifier = Modifier.wrapContentHeight(),
                    onClick = { viewModel.openSettings() },
                ) {
                    Text("Settings")
                }
                Spacer(modifier = Modifier.weight(1f))
                BoldButton(
                    modifier = Modifier.wrapContentHeight(),
                    onClick = { viewModel.onTrayAction() },
                ) {
                    Text(actionLabel)
                }
            }
        }
    }
}

var formatter = SimpleDateFormat("MMM dd hh:mm:ss a", Locale.getDefault())

@Composable
private fun KillFeedFragment(
    killList: ImmutableList<KillEvent>,
    onEventSelected: (String?) -> Unit,
) {
    FrameSlim(
        modifier = Modifier.fillMaxSize().padding(Padding.medium),
    ) {
        LazyColumn {
            items(killList) {
                val time = it.time?.let {
                    formatter.format(Date(it.toEpochMilliseconds()))
                } ?: ""

                KillItem(
                    modifier = Modifier.fillMaxWidth(),
                    killType = it.killType,
                    faction = it.faction,
                    attacker = it.attacker ?: "",
                    time = time,
                    weaponName = it.weaponName ?: "",
                    weaponImage = it.weaponImage,
                    onClick = { onEventSelected(it.characterId) },
                )
            }
        }
    }
}
