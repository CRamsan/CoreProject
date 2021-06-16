package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.item.Vehicle
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle_list_response(
    val vehicle_list: List<Vehicle>,
)
