package com.cramsan.framework.sample.android.app

/**
 *
 */
sealed class UIEvent {
    /**
     *
     */
    object Noop : UIEvent()

    /**
     *
     */
    class NextPage(val stockId: String) : UIEvent()
}
