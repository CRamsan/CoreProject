package com.cramsan.petproject.utils

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @BindingAdapter("app:observableVisibility")
    @JvmStatic
    fun setObservableVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }
}
