package com.cesarandres.ps2link.dbg.content.response

import com.cesarandres.ps2link.dbg.content.CharacterEvent

import java.util.ArrayList

class Characters_event_list_response {

    var characters_event_list: ArrayList<CharacterEvent>? = null
        private set

    fun setCharacter_event_list(characters_event_list: ArrayList<CharacterEvent>) {
        this.characters_event_list = characters_event_list
    }
}
