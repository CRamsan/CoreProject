package com.cramsan.petproject.feedback

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
import com.cramsan.framework.userevents.logEvent
import com.cramsan.petproject.appcore.model.AnimalType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel that manages the feedback screen.
 */
@HiltViewModel
class PlantFeedbackViewModel @Inject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    override val logTag: String
        get() = "PlantFeedbackViewModel"

    /**
     * Store the [AnimalType].
     */
    private val animal = MutableLiveData<AnimalType>()
    /**
     * Store a plantId as a [Long].
     */
    private val plantId = MutableLiveData<Long>()
    /**
     * Used for two-way databinding.
     * Store the selection for the Photo field.
     */
    val photo = MutableLiveData<Boolean>()
    /**
     * Used for two-way databinding.
     * Store the selection for the scientific name field.
     */
    val scientificName = MutableLiveData<Boolean>()
    /**
     * Used for two-way databinding.
     * Store the selection for the name field.
     */
    val name = MutableLiveData<Boolean>()
    /**
     * Used for two-way databinding.
     * Store the selection for the link field.
     */
    val link = MutableLiveData<Boolean>()
    /**
     * Used for two-way databinding.
     * Store the selection for the text field.
     */
    val text = MutableLiveData<String>()

    @Suppress("DEPRECATION")
    private val observableIsComplete = LiveEvent<CompletedEvent>()

    /**
     * Observable that emits an event when submitting feedback is completed
     * and therefore it is safe to close this screen.
     */
    fun isComplete(): LiveData<CompletedEvent> = observableIsComplete

    /**
     * Cancel the current screen.
     */
    fun cancel(@Suppress("UNUSED_PARAMETER") view: View) {
        logI("PlantFeedbackViewModel", "cancel")
        observableIsComplete.value = CompletedEvent(false)
    }

    /**
     * Submit the feedback.
     */
    fun sendFeedback(@Suppress("UNUSED_PARAMETER") view: View) {
        logI("PlantFeedbackViewModel", "sendFeedback")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "Animal:${animal.value?.name} - " +
                "PlantId:${plantId.value} - " +
                "Photo: ${photo.value} - " +
                "ScientifiName:${scientificName.value} - " +
                "Name:${name.value} - " +
                "Link:${link.value} - " +
                "Text:${text.value}"
            logEvent("PlantFeedbackViewModel", "Suggestion", mapOf("Data" to suggestion))
            viewModelScope.launch {
                observableIsComplete.value = CompletedEvent(true)
            }
        }
    }
}
