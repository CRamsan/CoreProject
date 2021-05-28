package com.cesarandres.ps2link

import android.os.Bundle
import com.cramsan.ps2link.core.models.CensusLang
import java.util.Locale

fun getLang() = Locale.getDefault().displayLanguage

fun getCurrentLang() = CensusLang.fromString(getLang())

fun Bundle.toMap(): Map<String, String> = this.keySet().map { it to get(it).toString() }.toMap()
