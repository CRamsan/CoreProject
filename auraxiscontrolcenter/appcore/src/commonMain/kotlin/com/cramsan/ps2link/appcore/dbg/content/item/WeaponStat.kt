package com.cramsan.ps2link.appcore.dbg.content.item

data class WeaponStat (

    var name: String? = null,
    var vehicle: String? = null,
    var imagePath: String? = null,
    var kills: Int = 0,
    var tr: Int = 0,
    var vs: Int = 0,
    var nc: Int = 0,
    var headshots: Int = 0,
    var vehicleKills: Int = 0,

            /*
    override fun compareTo(another: WeaponStat): Int {
        return if (another.kills - this.kills == 0) {
            if (another.headshots - this.headshots == 0) {
                another.vehicleKills - this.vehicleKills
            } else {
                another.headshots - this.headshots
            }
        } else {
            another.kills - this.kills
        }
    }
             */
)
