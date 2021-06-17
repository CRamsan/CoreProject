package com.cramsan.ps2link.appcore.census

import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.network.models.Namespace
import com.cramsan.ps2link.network.models.Verb
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

class DBGCensus {

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
        currentLang: CensusLang,
    ): UrlHolder {
        val baseUrl = "$ENDPOINT_URL/$SERVICE_ID/$verb/$namespace/$collection/"
        return UrlHolder(
            urlIdentifier = baseUrl,
            completeUrl = Url("$baseUrl/" + (identifier ?: "") + "?" + query.toString() + "&c:lang=" + currentLang.name.toLowerCase()),
        )
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
        currentLang: CensusLang
    ): UrlHolder {
        val baseUrl = "$ENDPOINT_URL/$SERVICE_ID/$verb/$namespace/$collection/"
        return UrlHolder(
            urlIdentifier = baseUrl,
            completeUrl = Url("$baseUrl/?" + urlParams + "&c:lang=" + currentLang.name.toLowerCase())
        )
    }

    /**
     * @return url to get server population
     */
    fun generateServerPopulationRequest(): UrlHolder {
        return UrlHolder(
            urlIdentifier = "$ENDPOINT_URL/$SERVICE_ID/json/status/ps2",
            completeUrl = Url("$ENDPOINT_URL/$SERVICE_ID/json/status/ps2"),
        )
    }

    companion object {
        const val SERVICE_ID = "s:PS2Link"
        const val ENDPOINT_URL = "https://census.daybreakgames.com"
        const val IMG = "img"
        const val ITEM = "item"
    }
}
