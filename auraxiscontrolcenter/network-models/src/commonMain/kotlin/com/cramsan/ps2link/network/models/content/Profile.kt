package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class Profile(
    val name: NameMultiLang? = null,
    val description: NameMultiLang? = null,
    val profile_id: String? = null,
    val profile_type_id: String? = null,
    val profile_type_description: String? = null,
    val faction_id: String? = null,
    val image_set_id: String? = null,
    val image_id: String? = null,
    val image_path: String? = null,
    val movement_speed: String? = null,
    val backpedal_speed_modifier: String? = null,
    val sprint_speed_modifier: String? = null,
    val strafe_speed_modifier: String? = null,
)
