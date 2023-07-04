package com.cramsan.ps2link.service.controller.census

import com.cramsan.ps2link.network.models.Namespace
import com.cramsan.ps2link.network.models.Verb
import com.cramsan.ps2link.network.models.content.CensusLang
import com.cramsan.ps2link.network.models.util.Collections
import com.cramsan.ps2link.network.models.util.QueryString
import io.ktor.http.Url

/**
 * This class will be in charge of formatting requests for DBG Census API and
 * retrieving the information. You can use the response directly from JSON or
 * they can be also automatically converted to objects to ease their
 * manipulation.
 *
 *
 * API Calls follow the following format:
 * /verb/game/collection/[identifier]?[queryString]
 *
 *
 * This class is been designed by following the design specified on
 * http://census.daybreakgames.com/.
 */

class DBGCensus(
    private val serviceId: String,
) {

    /**
     * @param verb action to realize, count or get
     * @param collection resource collection to retrieve
     * @param identifier id of the resource
     * @param query query with parameters for the search
     * @return the url to retrieve the requested resource
     */
    fun generateGameDataRequest(
        verb: Verb,
        collection: Collections.PS2Collection,
        identifier: String? = null,
        query: QueryString? = QueryString(),
        namespace: Namespace,
        currentLang: CensusLang?,
    ): Url {
        val baseUrl = "$ENDPOINT_URL/s:$serviceId/$verb/$namespace/$collection/"
        val langParam = currentLang?.name?.lowercase()?.let { "&c:lang=$it" }
        return Url("$baseUrl/" + (identifier ?: "") + "?" + query.toString() + (langParam ?: ""))
    }

    /**
     * @param urlParams that will be attached to the end of the default request body.
     * @return url to retrieve the requested resource
     */
    fun generateGameDataRequest(
        verb: Verb,
        collection: Collections.PS2Collection,
        urlParams: String,
        namespace: Namespace,
        currentLang: CensusLang,
    ): Url {
        val baseUrl = "$ENDPOINT_URL/s:$serviceId/$verb/$namespace/$collection/"
        return Url("$baseUrl/?" + urlParams + "&c:lang=" + currentLang.name.lowercase())
    }

    /**
     * @return url to get server population
     */
    fun generateServerPopulationRequest(): Url {
        return Url("$ENDPOINT_URL/s:$serviceId/json/status/ps2")
    }

    companion object {
        const val ENDPOINT_URL = "https://census.daybreakgames.com"
        const val IMG = "img"
        const val ITEM = "item"
    }
}
