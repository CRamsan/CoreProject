package com.cramsan.ps2link.network.ws.testgui.application

/**
 * This enum represents the different high-level states for the application.
 */
enum class ProgramMode {
    /**
     * State in which the UI is blocked for user interaction.
     */
    LOADING,
    /**
     * The application can handle user events. A user has not been selected.
     */
    NOT_CONFIGURED,
    /**
     * The application can handle user events.
     * A user has been selected.
     * We are listening for events.
     */
    RUNNING,
    /**
     * The application can handle user events.
     * A user has been selected.
     * We are not listening for events.
     */
    PAUSED
    ;
}
