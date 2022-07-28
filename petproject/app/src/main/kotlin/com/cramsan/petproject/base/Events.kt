package com.cramsan.petproject.base

import androidx.navigation.NavAction
import androidx.navigation.NavDestination
import com.cramsan.framework.core.BaseEvent

/**
 * Event to open the [url].
 */
data class OpenUrl(val url: String) : BaseEvent()

/**
 * Event to navigate to the [destination].
 */
data class NavigateToDestination(val destination: NavDestination) : BaseEvent()

/**
 * Event to navigate by using the provided [action].
 */
data class NavigateWithAction(val action: NavAction) : BaseEvent()
