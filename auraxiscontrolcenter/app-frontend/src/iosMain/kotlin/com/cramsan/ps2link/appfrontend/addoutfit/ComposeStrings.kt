package com.cramsan.ps2link.appfrontend.addoutfit

import androidx.compose.runtime.Composable

@Composable
actual fun TagText(): String = "Tag"

@Composable
actual fun OutfitNameText(): String = "Outfit Name"

@Composable
actual fun MemberCountText(memberCount: Int): String = "Members: $memberCount"
