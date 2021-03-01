package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/30/2021
 */
data class Outfit(
    val id: String,
    val name: String?,
    val tag: String?,
    val memberCount: Int,
    val namespace: Namespace,
)
