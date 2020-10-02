package com.cramsan.petproject.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
object BindingAdapters {

    @BindingAdapter("app:observableVisibility")
    @JvmStatic
    fun setObservableVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

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
