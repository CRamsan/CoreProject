package com.cesarandres.ps2link.dbg.volley

import android.graphics.Bitmap
import android.util.LruCache

import com.android.volley.toolbox.ImageLoader.ImageCache

/**
 * Bitmap Least-Recently-Used cache. It is basically a queue. Old entries get
 * deleted when the cache is full. If a entry is accessed it is moved to the
 * front of the queue
 */
class BitmapLruCache
/**
 * @param sizeInKiloBytes size in KB for the new cache
 */
@JvmOverloads constructor(sizeInKiloBytes: Int = defaultLruCacheSize) :
    LruCache<String, Bitmap>(sizeInKiloBytes), ImageCache {

    /* (non-Javadoc)
     * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object, java.lang.Object)
     */
    override fun sizeOf(key: String, value: Bitmap): Int {
        return value.rowBytes * value.height / 1024
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang.String)
     */
    override fun getBitmap(url: String): Bitmap? {
        return get(url)
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang.String, android.graphics.Bitmap)
     */
    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {

        /**
         * The size of the cache is based on the memory available
         *
         * @return the size in bytes for the cache
         */
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

                return maxMemory / 8
            }
    }
}
/**
 * Create the LRU cache with the default size
 */