package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.character.Name

class CharacterFriend : Comparable<CharacterFriend> {
    var name: Name? = null
    var character_id: String? = null
    var last_login_time: String? = null
    var online: Int = 0

    val isValid: Boolean
        get() = if (this.name != null && this.character_id != null && this.last_login_time != null) {
            true
        } else {
            false
        }

    override fun compareTo(another: CharacterFriend): Int {
        return if (this.online == another.online) {
            this.name!!.first_lower!!.compareTo(another.name!!.first_lower!!)
        } else {
            another.online - this.online
        }
    }
}