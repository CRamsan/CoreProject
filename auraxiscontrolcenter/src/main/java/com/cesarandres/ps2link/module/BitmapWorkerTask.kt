package com.cesarandres.ps2link.module

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.AsyncTask
import android.os.Environment
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.cesarandres.ps2link.ApplicationPS2Link
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference

/**
 * This async task will receive resize an image to a size that will fit inside
 * the screen for the device in both horizontal and vertical orientation. The
 * resulting image will be set as the background for the provided view
 */
class BitmapWorkerTask
/**
 * @param imageView View where to where the image will be set after resizing
 * @param context Activity that requested this task
 */
    (imageView: ImageView, private val context: Activity) : AsyncTask<String, Void, Bitmap>() {
    private val imageViewReference: WeakReference<ImageView>?
    private var data: String? = null

    init {
        // Use a WeakReference to ensure the ImageView can be garbage
        // collected
        this.imageViewReference = WeakReference(imageView)
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    override fun doInBackground(vararg params: String): Bitmap? {
        data = params[0]

        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory(),
            PUBLIC_DIR + File.separator + DOWNLOAD_DIR
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        val file = File(mediaStorageDir.absoluteFile.toString() + File.separator + data)
        if (!file.exists()) {
            val display = context.windowManager.defaultDisplay
            val size = Point()
            val width: Int
            val height: Int
            display.getSize(size)
            width = size.x
            height = size.y

            val ims: InputStream
            try {
                if (isCancelled) {
                    return null
                }
                ims = context.assets.open(data!!)
                val image = BitmapFactory.decodeStream(
                    ims,
                    null,
                    generateDecodeSampledOptionsFromResource(ims, width, height)
                )
                val bytes = ByteArrayOutputStream()
                image!!.compress(Bitmap.CompressFormat.JPEG, 85, bytes)

                val fos = FileOutputStream(file)
                fos.write(bytes.toByteArray())
                fos.close()
                ApplicationPS2Link.background = image
                return image
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } catch (e: RuntimeException) {
                e.printStackTrace()
                return null
            }
        } else {
            try {
                if (isCancelled) {
                    return null
                }
                val image = BitmapFactory.decodeFile(file.absolutePath)
                ApplicationPS2Link.background = image
                return image
            } catch (e: OutOfMemoryError) {
                System.gc()
                return null
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    override fun onPostExecute(bitmap: Bitmap?) {
        if (!isCancelled || imageViewReference != null && bitmap != null) {
            val imageView = imageViewReference!!.get()
            if (imageView != null) {
                imageView.setImageBitmap(bitmap)
                imageView.scaleType = ScaleType.CENTER_CROP
            }
        }
    }

    companion object {
        private val DOWNLOAD_DIR = ".cache"
        private val PUBLIC_DIR = "AuraxisControlCenter"

        /**
         * @param options Set of options to apply to the image transformation
         * @param reqWidth width of the resulting image
         * @param reqHeight height of the resulting image
         * @return the biggest ratio for the given size
         */
        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                // Calculate ratios of height and width to requested height and
                // width
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

                // Choose the smallest ratio as inSampleSize value, this will
                // guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }

            return inSampleSize
        }

        /**
         * @param ims Stream from the image or resource to resize
         * @param reqWidth resulting image width
         * @param reqHeight resulting image height
         * @return
         */
        @Throws(IOException::class)
        fun generateDecodeSampledOptionsFromResource(
            ims: InputStream,
            reqWidth: Int,
            reqHeight: Int
        ): BitmapFactory.Options {

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(ims, null, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            ims.reset()
            return options
        }
    }
}
