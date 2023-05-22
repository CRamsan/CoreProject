package com.cramsan.petproject.suggestion

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LiveEvent
import com.cramsan.framework.logging.logI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel that manages the suggestion screen.
 */
@HiltViewModel
class PlantSuggestionViewModel @Inject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    /**
     * 2-way databinding. Allows for a user to input a string to suggest a new plant.
     */
    val observableText = MutableLiveData<String>()
    /**
     * Observable [LiveData] that emits a [CompletedEvent] to close this screen.
     */
    val observableIsComplete = LiveEvent<CompletedEvent>()
    /**
     * Represents the toxicity value for cats selected by the user.
     */
    val observableSelectedCatToxicityRes = MutableLiveData<Int>()
    /**
     * Represents the toxicity value for dogs selected by the user.
     */
    val observableSelectedDogToxicityRes = MutableLiveData<Int>()

    override val logTag: String
        get() = "PlantSuggestionViewModel"

    /**
     * Callback to be called when the cancel button is pressed.
     */
    fun cancel(@Suppress("UNUSED_PARAMETER") view: View) {
        observableIsComplete.value = CompletedEvent(false)
    }

    /**
     * Callback to be called when the save button is pressed.
     */
    fun save(@Suppress("UNUSED_PARAMETER") view: View) {
        logI("PlantSuggestionViewModel", "savePlant")
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch {
                observableIsComplete.value = CompletedEvent(true)
            }
        }
    }
}
