package com.cramsan.ps2link.network.ws

/**
 * The environment that the client will connect to. The client can only connect to a single [Environment]
 * at a time.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 * @author cramsan
 */
enum class Environment {
    PS2,
    PS2PS4US,
    PS2PS4EU,
    ;

    override fun toString(): String {
        return name.lowercase()
    }
}
