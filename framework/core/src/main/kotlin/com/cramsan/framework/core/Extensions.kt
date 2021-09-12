package com.cramsan.framework.core

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Easily cast a [MutableLiveData] into it's read-only [LiveData] version.
 */
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

/**
 * Enforce that an activity is associated with this [BaseFragment] and that it is of class
 * [AppCompatActivity]. If it is not, an exception will be thrown.
 */
fun BaseFragment.requireAppCompatActivity(): AppCompatActivity {
    return requireActivity() as AppCompatActivity
}
