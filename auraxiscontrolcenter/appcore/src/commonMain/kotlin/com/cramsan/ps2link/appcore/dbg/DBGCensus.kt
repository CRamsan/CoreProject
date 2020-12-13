package com.cramsan.ps2link.appcore.dbg

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.ps2link.appcore.dbg.util.Collections.PS2Collection
import com.cramsan.ps2link.appcore.dbg.util.QueryString
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
    private val eventLogger: EventLoggerInterface,
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
        collection: PS2Collection,
        identifier: String? = "",
        query: QueryString? = QueryString(),
        namespace: Namespace,
        currentLang: CensusLang,
    ): Url {
        return Url(
            ENDPOINT_URL + "/" + SERVICE_ID + "/" + verb.toString() + "/" + namespace + "/" + collection.toString() + "/" +
                identifier + "?" + query.toString() + "&c:lang=" + currentLang.name.toLowerCase()
        )
    }

    /**
     * @param urlParams that will be attached to the end of the default request body.
     * @return url to retrieve the requested resource
     */
    fun generateGameDataRequest(
        urlParams: String,
        namespace: Namespace,
        currentLang: CensusLang
    ): Url {
        return Url(ENDPOINT_URL + "/" + SERVICE_ID + "/" + Verb.GET + "/" + namespace + "/" + urlParams + "&c:lang=" + currentLang.name.toLowerCase())
    }

    companion object {
        const val SERVICE_ID = "s:PS2Link"
        const val ENDPOINT_URL = "https://census.daybreakgames.com"
        const val IMG = "img"
        const val ITEM = "item"
    }
}
