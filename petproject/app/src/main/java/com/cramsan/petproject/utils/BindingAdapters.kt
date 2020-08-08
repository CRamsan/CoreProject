package com.cramsan.petproject.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cramsan.petproject.PetProjectApplication
import org.kodein.di.DIAware
import org.kodein.di.instance

object BindingAdapters : DIAware {

    override val di by lazy { PetProjectApplication.getInstance().di }

    val context: Context by instance()

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
