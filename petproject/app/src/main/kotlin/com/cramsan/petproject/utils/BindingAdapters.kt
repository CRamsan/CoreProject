package com.cramsan.petproject.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * List of binding adapters.
 * https://developer.android.com/topic/libraries/data-binding/binding-adapters
 */
object BindingAdapters {

    /**
     * Set the observability of the view from a LiveData<Int>.
     */
    @BindingAdapter("app:observableVisibility")
    @JvmStatic
    fun setObservableVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    /**
     * Convenience method that binds a [formatString] and their [formatArg] to a [TextView].
     */
    @BindingAdapter("formatString", "formatArg")
    @JvmStatic
    fun setFormatText(textView: TextView, formatString: String?, formatArg: String?) {
        if (formatString == null || formatArg == null) {
            textView.text = ""
            return
        }
        textView.text = formatString.format(formatArg)
    }
}
