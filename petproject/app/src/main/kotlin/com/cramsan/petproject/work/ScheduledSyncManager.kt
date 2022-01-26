package com.cramsan.petproject.work

/**
 * Sync mamnager to launch syncs on the background.
 */
interface ScheduledSyncManager {
    /**
     * Start the work to sync the catalog.
     */
    fun startWork()
}
