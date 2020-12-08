package com.cramsan.ps2link.appcore.dbg.content

data class CharacterFriendRoot (
    var character_id: String? = null,
    var name: String? = null,
    var friend_list: List<CharacterFriend>? = null,
)