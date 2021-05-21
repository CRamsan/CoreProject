package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.item.Item
import kotlinx.serialization.Serializable

@Serializable
data class Item_list_response(
    var item_list: List<Item>,
)
