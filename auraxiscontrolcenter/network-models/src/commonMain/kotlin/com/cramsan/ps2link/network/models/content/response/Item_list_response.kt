package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.Item
import com.cramsan.ps2link.network.models.content.Vehicle

data class Item_list_response(
    var item_list: List<Item>? = null,
    var vehicle_list: List<Vehicle>? = null,
)
/*
fun setEvent_list(vehicle_list: List<Vehicle>) {
    this.vehicle_list = vehicle_list
}
 */
