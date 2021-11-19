package com.cramsan.stranded.lib.client.ui.game.widget

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.game.models.crafting.Shelter

interface ShelterWidget : UIComponent {
    fun setShelterList(shelterList: List<Shelter>)
}
