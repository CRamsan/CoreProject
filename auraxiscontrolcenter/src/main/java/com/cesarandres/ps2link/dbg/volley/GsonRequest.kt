package com.cesarandres.ps2link.dbg.volley

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import com.cesarandres.ps2link.dbg.util.Logger
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.

 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */

class GsonRequest<T> (
    url: String,
    private val clazz: Class<T>,
    private val headers: Map<String, String>?,
    private val listener: Listener<T>,
    errorListener: ErrorListener
) : Request<T>(Request.Method.GET, url, errorListener) {
    private val gson = Gson()

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#getHeaders()
     */
    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return headers ?: super.getHeaders()
    }

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#deliverResponse(java.lang.Object)
     */
    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.
     * NetworkResponse)
     */
    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        try {
            val json = String(response.data, charset(HttpHeaderParser.parseCharset(response.headers)))
            val jsonObject = gson.fromJson(json, clazz)
            Logger.log(Log.DEBUG, "GsonRequest", "Request URL: $url")
            Logger.log(Log.DEBUG, "GsonRequest", "JSON Response: $json")
            return Response.success<Any>(
                jsonObject,
                HttpHeaderParser.parseCacheHeaders(response)
            ) as Response<T>
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            return Response.error(ParseError(e))
        }
    }
}
