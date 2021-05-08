package com.cramsan.framework.core

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun BaseFragment.requireAppCompatActivity(): AppCompatActivity {
    return requireActivity() as AppCompatActivity
}
