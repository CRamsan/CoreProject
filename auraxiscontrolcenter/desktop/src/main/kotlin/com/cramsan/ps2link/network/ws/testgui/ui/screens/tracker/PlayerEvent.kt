package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

sealed class PlayerEvent {
    data class BattleRankUp(
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
        val battleRank: String? = null,
        val characterId: String? = null,
    ) : PlayerEvent()

    data class Death(
        val attackerCharacterId: String? = null,
        val attackerCharacterName: String? = null,
        val attackerCharacterRank: Int? = null,
        val attackerCharacterFaction: String? = null,
        val attackerFireModeId: String? = null,
        val attackerLoadoutId: String? = null,
        val attackerVehicleId: String? = null,
        val attackerWeaponId: String? = null,
        val attackerWeaponName: String? = null,
        val attackerWeaponImageUrl: String? = null,
        val characterId: String? = null,
        val characterName: String? = null,
        val characterRank: Int? = null,
        val characterFaction: String? = null,
        val characterLoadoutId: String? = null,
        val isCritical: String? = null,
        val isHeadshot: String? = null,
        val timestamp: String? = null,
        val vehicleId: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
        val time: String? = null,
    ) : PlayerEvent()
}
