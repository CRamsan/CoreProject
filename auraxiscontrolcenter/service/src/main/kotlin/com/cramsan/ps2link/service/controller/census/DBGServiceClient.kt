package com.cramsan.ps2link.service.controller.census

import com.cramsan.ps2link.network.models.Namespace
import com.cramsan.ps2link.network.models.Verb
import com.cramsan.ps2link.network.models.content.response.Character_list_response
import com.cramsan.ps2link.network.models.content.response.Item_list_response
import com.cramsan.ps2link.network.models.content.response.ProfileResponse
import com.cramsan.ps2link.network.models.content.response.Server_response
import com.cramsan.ps2link.network.models.util.Collections
import com.cramsan.ps2link.network.models.util.QueryString
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DBGServiceClient(
    private val census: DBGCensus,
    private val http: HttpClient,
) {
    suspend fun getCharacter(
        characterId: String,
        namespace: Namespace,
    ): Character_list_response? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.CHARACTER,
            characterId,
            QueryString(),
            namespace,
            currentLang = null,
        )
        return http.get(url).body()
    }

    suspend fun getWorlds(
        namespace: Namespace,
    ): Server_response? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.WORLD,
            null,
            QueryString(),
            namespace,
            currentLang = null,
        )
        return http.get(url).body()
    }

    suspend fun getProfiles(
        namespace: Namespace,
    ): ProfileResponse? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.PROFILE,
            null,
            QueryString().AddCommand(QueryString.QueryCommand.LIMIT, "100"),
            namespace,
            currentLang = null,
        )
        return http.get(url).body()
    }

    suspend fun getItem(
        itemId: String,
        namespace: Namespace,
    ): Item_list_response? {
        val url = census.generateGameDataRequest(
            Verb.GET,
            Collections.PS2Collection.ITEM,
            itemId,
            QueryString(),
            namespace,
            currentLang = null,
        )
        return http.get(url).body()
    }
}
