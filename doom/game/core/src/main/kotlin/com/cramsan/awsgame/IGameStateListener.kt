package com.cramsan.awsgame

/**
 * IGameListener allows the caller to listen for Game-wide events. This event are related to gameplay
 * or screen changes and should not be used for any purpose other than testing.
 */
interface IGameStateListener {
    /**
     * Called when the create method from MyGdxGame has completed.
     */
    fun onGameCreated()

    /**
     * Event called after the destroyed method is called.
     */
    fun onGameDestroyed()
}
