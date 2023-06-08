package com.cramsan.ps2link.appfrontend.outfitpager.outfit

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.ui.R
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date

@Composable
actual fun OutfitCreatedText(): String {
    return stringResource(R.string.text_created)
}

@Composable
actual fun OutfitTagText(tag: String): String {
    return stringResource(R.string.text_outfit_tag, tag)
}

@Composable
actual fun OutfitLeaderText(): String {
    return stringResource(R.string.text_leader)
}

@Composable
actual fun OutfitMembersText(): String {
    return stringResource(R.string.text_members)
}

