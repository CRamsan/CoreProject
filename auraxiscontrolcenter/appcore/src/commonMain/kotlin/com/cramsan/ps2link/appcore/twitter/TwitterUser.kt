package com.cramsan.ps2link.appcore.twitter

/**
 * @Author cramsan
 * @created 2/10/2021
 */
enum class TwitterUser(val handle: String) {
    PLANETSIDE_2("planetside2"),
    PS2_DAILY_DEALS("PS2DailyDeals"),
    WREL_PLAYS("WrelPlays"),
    ANDY_SITES("AndySites");

    companion object {
        private val enumMapping: Map<String, TwitterUser> by lazy {
            values().associateBy { it.handle }
        }

        fun fromString(code: String?): TwitterUser? {
            return enumMapping[code]
        }
    }
}
