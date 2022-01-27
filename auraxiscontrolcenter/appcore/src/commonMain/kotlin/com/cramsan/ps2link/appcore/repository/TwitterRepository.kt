package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.core.models.PS2Tweet
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author cramsan
 * @created 2/9/2021
 *
 * Manage twitter posts and users. The implementation can decide from which users to fetch posts from.
 */
interface TwitterRepository {

    /**
     * Get a map that contains a list of twitter usernames and if we are currently fetching their posts.
     */
    suspend fun getTwitterUsers(): Map<String, Boolean>

    /**
     * Change the follow status for a user.
     */
    suspend fun setFollowStatus(user: String, follow: Boolean)

    /**
     * Get a list of twits for the followed users.
     */
    suspend fun getTweets(): PS2HttpResponse<List<PS2Tweet>>

    /**
     * Get an observable flow that emits a list of twits for the followed users.
     */
    fun getTweetsAsFlow(): StateFlow<PS2HttpResponse<List<PS2Tweet>>?>

    /**
     * Get an observable flow that emits a map of followed users and their follow status.
     */
    fun getTwitterUsersAsFlow(): StateFlow<Map<String, Boolean>>
}
