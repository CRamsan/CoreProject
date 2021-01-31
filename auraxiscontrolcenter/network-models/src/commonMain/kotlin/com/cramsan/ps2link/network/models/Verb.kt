package com.cramsan.ps2link.network.models

enum class Verb private constructor(private val verb: String) {
    GET("get"), COUNT("count");

    override fun toString(): String {
        return this.verb
    }
}
