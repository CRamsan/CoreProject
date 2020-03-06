package com.cesarandres.ps2link.dbg.content.character

class Stat {
    var all_time: String? = null
    var last_save: String? = null
    var last_save_date: String? = null
    var one_life_max: String? = null
    var stat_name: String? = null
    var day: Day? = null
    var week: Week? = null
    var month: Month? = null

    var today: Float
        get() = java.lang.Float.parseFloat(this.day!!.d01!!)
        set(value) {
            this.day!!.d01 = java.lang.Float.toString(value)
        }

    var thisWeek: Float
        get() = java.lang.Float.parseFloat(this.week!!.w01!!)
        set(value) {
            this.week!!.w01 = java.lang.Float.toString(value)
        }

    var thisMonth: Float
        get() = java.lang.Float.parseFloat(this.month!!.m01!!)
        set(value) {
            this.month!!.m01 = java.lang.Float.toString(value)
        }

    inner class Day {
        var d01: String? = null
        var d02: String? = null
        var d03: String? = null
        var d04: String? = null
        var d05: String? = null
        var d06: String? = null
        var d07: String? = null
        var d08: String? = null
        var d09: String? = null
        var d10: String? = null
        var d12: String? = null
        var d13: String? = null
        var d14: String? = null
        var d15: String? = null
        var d16: String? = null
        var d17: String? = null
        var d18: String? = null
        var d19: String? = null
        var d20: String? = null
        var d21: String? = null
        var d22: String? = null
        var d23: String? = null
        var d24: String? = null
        var d25: String? = null
        var d26: String? = null
        var d27: String? = null
        var d28: String? = null
        var d29: String? = null
        var d30: String? = null
        var d31: String? = null
    }

    inner class Week {
        var w01: String? = null
        var w02: String? = null
        var w03: String? = null
        var w04: String? = null
        var w05: String? = null
        var w06: String? = null
        var w07: String? = null
        var w08: String? = null
        var w09: String? = null
        var w10: String? = null
        var w12: String? = null
        var w13: String? = null
    }

    inner class Month {
        var m01: String? = null
        var m02: String? = null
        var m03: String? = null
        var m04: String? = null
        var m05: String? = null
        var m06: String? = null
        var m07: String? = null
        var m08: String? = null
        var m09: String? = null
        var m10: String? = null
        var m12: String? = null
    }
}
