package com.cesarandres.ps2link

import android.os.Bundle
import com.cramsan.ps2link.core.models.CensusLang
import java.util.Locale

fun getLang() = Locale.getDefault().displayLanguage

fun getCurrentLang() = CensusLang.fromString(getLang())

/**
 * Transform a [Bundle] into a map. Used for telemetry purposes.
 */
@Suppress("DEPRECATION")
fun Bundle.toMetadataMap(): Map<String, String> = this.keySet().associateWith { get(it).toString() }
