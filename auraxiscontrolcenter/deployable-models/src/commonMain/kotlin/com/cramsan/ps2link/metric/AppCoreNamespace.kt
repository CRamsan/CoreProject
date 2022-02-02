package com.cramsan.ps2link.metric

import com.cramsan.framework.metrics.MetricNamespace

sealed class AppCoreNamespace : MetricNamespace

object HttpNamespace : AppCoreNamespace() {
    override val identifier = "Http"

    enum class Api {
        PROFILE,
        PROFILE_LIST,
        FRIEND_LIST,
        OUTFIT,
        OUTFIT_LIST,
        OUTFIT_MEMBER_LIST,
        OUTFIT_MEMBERS_ONLINE,
        WEAPON_LIST,
        KILL_LIST,
        WEAPONS_FOR_KILL_LIST,
        VEHICLES_FOR_KILL_LIST,
        STAT_LIST,
        REDDIT,
        SERVER_METADATA,
        SERVER_LIST,
        SERVER_POP,
        EXPERIENCE_RANK,
    }
}

object ApplicationNamespace : AppCoreNamespace() {
    override val identifier = "Application"

    enum class Event {
        LAUNCH
    }
}
