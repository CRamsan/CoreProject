package com.cesarandres.ps2link

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask

import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.volley.BitmapLruCache

import java.io.File
import java.util.Arrays
import java.util.Comparator
import java.util.Locale

class ApplicationPS2Link : Application() {

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    override fun onCreate() {
        super.onCreate()


        if (volley == null) {
            ApplicationPS2Link.volley = Volley.newRequestQueue(this)
        }
        if (mImageLoader == null) {
            mImageLoader = ImageLoader(
                ApplicationPS2Link.volley,
                BitmapLruCache()
            )
        }

        if (getCacheSize(this) > MAX_CACHE_SIZE) {
            ClearCache().execute()
        }

        val lang = Locale.getDefault().language
        DBGCensus.currentLang = DBGCensus.CensusLang.EN
        for (clang in DBGCensus.CensusLang.values()) {
            if (lang.equals(clang.name, ignoreCase = true)) {
                DBGCensus.currentLang = clang
            }
        }
    }

    /**
     * This enum holds all the different activity modes, there is one for each
     * main fragment that is used.
     */
    enum class ActivityMode {
        ACTIVITY_ADD_OUTFIT,
        ACTIVITY_ADD_PROFILE,
        ACTIVITY_MEMBER_LIST,
        ACTIVITY_OUTFIT_LIST,
        ACTIVITY_PROFILE,
        ACTIVITY_PROFILE_LIST,
        ACTIVITY_SERVER_LIST,
        ACTIVITY_TWITTER,
        ACTIVITY_LINK_MENU,
        ACTIVITY_MAIN_MENU,
        ACTIVITY_REDDIT,
        ACTIVITY_ABOUT,
        ACTIVITY_SETTINGS
    }


    /**
     * This enum holds the reference for all four background types.
     */
    enum class WallPaperMode {
        PS2, NC, TR, VS
    }

    /**
     * This AsyncTask will delete all the files from the cache.
     */
    inner class ClearCache : AsyncTask<String, Int, Boolean>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {}

        override fun doInBackground(vararg args: String): Boolean? {
            val fileList = fileList()
            for (file in fileList) {
                deleteFile(file)
            }
            return null
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Boolean?) {

        }
    }

    /**
     * This AsyncTask will delete the least used files from the cache until it reaches its maximum size allowed.
     */
    inner class TrimCache : AsyncTask<String, Int, Boolean>() {

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        override fun onPreExecute() {}

        override fun doInBackground(vararg args: String): Boolean? {

            val files = filesDir.listFiles()

            Arrays.sort(files!!) { f1, f2 ->
                java.lang.Long.valueOf(f1.lastModified()).compareTo(f2.lastModified())
            }

            var i = 0
            while (getCacheSize(applicationContext) > MAX_CACHE_SIZE && i < files.size) {
                deleteFile(files[i].absolutePath)
                i++
            }

            return null
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Boolean?) {

        }
    }

    companion object {

        internal val ACTIVITY_MODE_KEY = "activity_mode"
        private val MAX_CACHE_SIZE = 2000000 //2 MB
        var volley: RequestQueue? = null
        var mImageLoader: ImageLoader? = null
        /**
         * @return the bitmap holding the data for the background for all activities
         */
        /**
         * @param background bitmap that will be used as background for all activities
         */
        var background: Bitmap? = null
        /**
         * @return the current Wallpaper mode
         */
        /**
         * @param wallpaper the wallpaper mode that matches the current wallpaper. This
         * method should only be called after the wallpaper has been set
         */
        var wallpaperMode = WallPaperMode.PS2

        /**
         * @return the size in bytes of all the files in the cache
         */
        fun getCacheSize(context: Context): Long {
            val fileList = context.fileList()
            var size: Long = 0
            for (file in fileList) {
                size += File(file).length()
            }
            return size
        }
    }
}