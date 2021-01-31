package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/22/2021
 */
enum class LoginStatus(val code: String) {
    ONLINE("0"),
    OFFLINE("1"),
    UNKNOWN("-1");

    companion object {

        private val enumMapping: Map<String, LoginStatus> by lazy {
            values().associateBy { it.code }
        }

        fun fromString(code: String?): LoginStatus {
            return enumMapping[code] ?: UNKNOWN
        }
    }
}
