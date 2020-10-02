package com.cesarandres.ps2link.dbg.content.response

import com.cesarandres.ps2link.dbg.content.Item
import com.cesarandres.ps2link.dbg.content.Vehicle
import java.util.ArrayList

class Item_list_response {
    var item_list: ArrayList<Item>? = null
    var vehicle_list: ArrayList<Vehicle>? = null
        private set

    fun setEvent_list(vehicle_list: ArrayList<Vehicle>) {
        this.vehicle_list = vehicle_list
    }
}
