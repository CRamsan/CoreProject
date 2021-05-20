package com.cesarandres.ps2link

import com.cramsan.ps2link.core.models.CensusLang
import java.util.Locale

fun getLang() = Locale.getDefault().displayLanguage

fun getCurrentLang() = CensusLang.fromString(getLang())
