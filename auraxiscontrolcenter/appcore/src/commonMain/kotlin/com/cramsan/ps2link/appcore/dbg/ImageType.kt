package com.cramsan.ps2link.appcore.dbg;

enum class ImageType private constructor(private val imagetype: String) {
        PAPERDOLL("paperdoll"), HEADSHOT("headshot");

        override fun toString(): String {
            return this.imagetype
        }
    }
