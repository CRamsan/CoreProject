package com.cramsan.ps2link.appcore.dbg

enum class Verb private constructor(private val verb: String) {
    GET("get"), COUNT("count");

    override fun toString(): String {
        return this.verb
    }
}
