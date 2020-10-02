package com.cesarandres.ps2link.module.reddit

import com.google.gson.annotations.Expose
import java.util.ArrayList

class Data {

    /**
     * @return The modhash
     */
    /**
     * @param modhash The modhash
     */
    @Expose
    var modhash: String? = null
    /**
     * @return The children
     */
    /**
     * @param children The children
     */
    @Expose
    var children: List<Child> = ArrayList()
    /**
     * @return The after
     */
    /**
     * @param after The after
     */
    @Expose
    var after: String? = null
    /**
     * @return The before
     */
    /**
     * @param before The before
     */
    @Expose
    var before: Any? = null
}
