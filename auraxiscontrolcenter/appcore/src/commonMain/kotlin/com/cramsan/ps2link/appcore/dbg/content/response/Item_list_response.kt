package com.cramsan.ps2link.appcore.dbg.content.response

import com.cramsan.ps2link.appcore.dbg.content.Item
import com.cramsan.ps2link.appcore.dbg.content.Vehicle

data class Item_list_response (
    var item_list: List<Item>? = null,
    var vehicle_list: List<Vehicle>? = null,
)
/*
fun setEvent_list(vehicle_list: List<Vehicle>) {
    this.vehicle_list = vehicle_list
}
 */
