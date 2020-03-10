package com.cesarandres.ps2link.dbg

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue

import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.cesarandres.ps2link.ApplicationPS2Link
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection
import com.cesarandres.ps2link.dbg.util.Logger
import com.cesarandres.ps2link.dbg.util.QueryString
import com.cesarandres.ps2link.dbg.volley.GsonRequest
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

import java.net.MalformedURLException
import java.net.URL

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

class DBGCensus(context: Context) : KodeinAware {

    override val kodein by kodein(context)
    private val eventLogger: EventLoggerInterface by instance()
    private val threadUtil: ThreadUtilInterface by instance()
    private val volley: RequestQueue by instance()

    var currentNamespace = Namespace.PS2PC
    var currentLang = CensusLang.EN

    /**
     * @param verb       action to realize, count or get
     * @param collection resource collection to retrieve
     * @param identifier id of the resource
     * @param query      query with parameters for the search
     * @return the url to retrieve the requested resource
     */
    fun generateGameDataRequest(
        verb: Verb,
        collection: PS2Collection,
        identifier: String?,
        query: QueryString?
    ): URL? {
        var identifier = identifier
        var query = query
        if (identifier == null) {
            identifier = ""
        }
        if (query == null) {
            query = QueryString()
        }
        var requestDataURL: URL? = null
        try {
            requestDataURL = URL(
                ENDPOINT_URL + "/" + SERVICE_ID + "/" + verb.toString() + "/" + currentNamespace + "/" + collection.toString() + "/"
                        + identifier + "?" + query.toString() + "&c:lang=" + currentLang.name.toLowerCase()
            )
        } catch (e: MalformedURLException) {
            Logger.log(Log.ERROR, "DBGCensus", "There was a problem creating the URL")
        }

        return requestDataURL
    }

    /**
     * @param urlParams that will be attached to the end of the default request body.
     * @return url to retrieve the requested resource
     */
    fun generateGameDataRequest(urlParams: String): URL? {
        var requestDataURL: URL? = null
        try {
            requestDataURL =
                URL(ENDPOINT_URL + "/" + SERVICE_ID + "/" + Verb.GET + "/" + currentNamespace + "/" + urlParams + "&c:lang=" + currentLang.name.toLowerCase())
        } catch (e: MalformedURLException) {
            Logger.log(Log.ERROR, "DBGCensus", "There was a problem creating the URL")
        }

        return requestDataURL
    }

    /**
     * @param url           the url to request
     * @param responseClass the class to which retrieve data will be serialized into
     * @param success       run this on success
     * @param error         run this when the request fails
     * @param caller        this is used to tag the call. Usually a fragment or activity
     * is a good tag
     */
    fun <T>sendGsonRequest(
        url: String,
        responseClass: Class<T>,
        success: Listener<T>,
        error: ErrorListener,
        caller: Any
    ) {
        val gsonOject = GsonRequest(url, responseClass, null, success, error)
        gsonOject.setTag(caller)
        volley.add(gsonOject)
    }

    enum class CensusLang {
        DE, EN, ES, FR, IT, TR
    }

    enum class Verb private constructor(private val verb: String) {
        GET("get"), COUNT("count");

        override fun toString(): String {
            return this.verb
        }
    }

    enum class Namespace private constructor(private val namespace: String) {
        PS2PC("ps2:v2"),
        PS2PS4US("ps2ps4us:v2"),
        PS2PS4EU("ps2ps4eu:v2");

        override fun toString(): String {
            return this.namespace
        }
    }

    enum class ImageType private constructor(private val imagetype: String) {
        PAPERDOLL("paperdoll"), HEADSHOT("headshot");

        override fun toString(): String {
            return this.imagetype
        }
    }

    companion object {
        const val SERVICE_ID = "s:PS2Link"
        const val ENDPOINT_URL = "https://census.daybreakgames.com"
        const val IMG = "img"
        const val ITEM = "item"
    }
}