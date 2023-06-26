package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

sealed class PlayerEvent {
    data class AchievementEarned(
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
        val achievementId: String? = null,
    ) : PlayerEvent()

    data class BattleRankUp(
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
        val battleRank: String? = null,
        val characterId: String? = null,
    ) : PlayerEvent()

    data class Death(
        val attackerCharacterId: String? = null,
        val attackerFireModeId: String? = null,
        val attackerLoadoutId: String? = null,
        val attackerVehicleId: String? = null,
        val attackerWeaponId: String? = null,
        val characterId: String? = null,
        val characterLoadoutId: String? = null,
        val isCritical: String? = null,
        val isHeadshot: String? = null,
        val timestamp: String? = null,
        val vehicleId: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class GainExperience(
        val amount: String? = null,
        val characterId: String? = null,
        val experienceId: String? = null,
        val loadoutId: String? = null,
        val otherId: String? = null,
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class ItemAdded(
        val characterId: String? = null,
        val context: String? = null,
        val itemCount: String? = null,
        val itemId: String? = null,
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class PlayerFacilityCapture(
        val characterId: String? = null,
        val facilityId: String? = null,
        val outfitId: String? = null,
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class PlayerFacilityDefend(
        val characterId: String? = null,
        val facilityId: String? = null,
        val outfitId: String? = null,
        val timestamp: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class SkillAdded(
        val timestamp: String? = null,
        val zoneId: String? = null,
        val characterId: String? = null,
        val skillId: String? = null,
    ) : PlayerEvent()

    data class VehicleDestroy(
        val attackerCharacterId: String? = null,
        val attackerLoadoutId: String? = null,
        val attackerVehicleId: String? = null,
        val attackerWeaponId: String? = null,
        val characterId: String? = null,
        val eventName: String? = null,
        val facilityId: String? = null,
        val factionId: String? = null,
        val timestamp: String? = null,
        val vehicleId: String? = null,
        val worldId: String? = null,
        val zoneId: String? = null,
    ) : PlayerEvent()

    data class PlayerLogin(
        val timestamp: String? = null,
        val zoneId: String? = null,
        val characterId: String? = null,
    ) : PlayerEvent()

    data class PlayerLogout(
        val timestamp: String? = null,
        val zoneId: String? = null,
        val characterId: String? = null,
    ) : PlayerEvent()
}
