package com.cramsan.stranded.lib.repository

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    var name: String,
    var readyToStart: Boolean
)
