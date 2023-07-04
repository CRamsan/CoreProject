package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.Profile
import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class ProfileResponse(
    val profile_list: List<Profile>,
)
