package com.cramsan.petproject.feedback

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.logI
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.logMetric
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantFeedbackViewModel @Inject constructor(
    application: Application,
    eventLogger: EventLoggerInterface,
    metricsClient: MetricsInterface,
    threadUtil: ThreadUtilInterface,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    override val logTag: String
        get() = "PlantFeedbackViewModel"

    val animal = MutableLiveData<AnimalType>()
    val plantId = MutableLiveData<Long>()
    val photo = MutableLiveData<Boolean>()
    val scientificName = MutableLiveData<Boolean>()
    val name = MutableLiveData<Boolean>()
    val link = MutableLiveData<Boolean>()
    val text = MutableLiveData<String>()

    private val observableIsComplete = LiveEvent<CompletedEvent>()

    fun isComplete() = observableIsComplete

    fun cancel(view: View) {
        logI("PlantFeedbackViewModel", "cancel")
        observableIsComplete.value = CompletedEvent(false)
    }

    fun sendFeedback(view: View) {
        logI("PlantFeedbackViewModel", "sendFeedback")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "Animal:${animal.value?.name} - " +
                "PlantId:${plantId.value} - " +
                "Photo: ${photo.value} - " +
                "ScientifiName:${scientificName.value} - " +
                "Name:${name.value} - " +
                "Link:${link.value} - " +
                "Text:${text.value}"
            logMetric("PlantFeedbackViewModel", "Suggestion", mapOf("Data" to suggestion))
            viewModelScope.launch {
                observableIsComplete.value = CompletedEvent(true)
            }
        }
    }
}
