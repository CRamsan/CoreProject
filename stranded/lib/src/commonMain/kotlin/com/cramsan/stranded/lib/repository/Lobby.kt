package com.cramsan.stranded.lib.repository

import kotlinx.serialization.Serializable

@Serializable
data class Lobby(
    val id: String,
    val name: String,
    val players: MutableList<String>
)
