package com.cramsan.ps2link.appcore.repository

import com.cramsan.ps2link.appcore.network.PS2HttpResponse
import com.cramsan.ps2link.appcore.twitter.TwitterUser
import com.cramsan.ps2link.core.models.PS2Tweet
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author cramsan
 * @created 2/9/2021
 */
interface TwitterRepository {

    suspend fun getTwitterUsers(): Map<TwitterUser, Boolean>

    suspend fun setFollowStatus(user: TwitterUser, follow: Boolean)

    suspend fun getTweets(): PS2HttpResponse<List<PS2Tweet>>

    fun getTweetsAsFlow(): StateFlow<PS2HttpResponse<List<PS2Tweet>>?>

    fun getTwitterUsersAsFlow(): StateFlow<Map<TwitterUser, Boolean>>
}
