package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus

@Composable
expect fun KillType.toText(): String

@Composable
expect fun unknownString(): String

@Composable
expect fun LoginStatus.toText(): String

@Composable
expect fun ServerStatus.toText(): String

@Composable
expect fun Population.toText(): String
