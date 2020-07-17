package com.cramsan.petproject.feedback

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantFeedbackViewModel(application: Application) : BaseViewModel(application) {

    override val logTag: String
        get() = "PlantFeedbackViewModel"

    val animal = MutableLiveData<AnimalType>()
    val plantId = MutableLiveData<Long>()
    val photo = MutableLiveData<Boolean>()
    val scientificName = MutableLiveData<Boolean>()
    val name = MutableLiveData<Boolean>()
    val link = MutableLiveData<Boolean>()
    val text = MutableLiveData<String>()

    private val observableIsComplete = MutableLiveData<Boolean>()

    fun isComplete(): LiveData<Boolean> {
        return observableIsComplete
    }

    fun cancel(view: View) {
        eventLogger.log(Severity.INFO, "PlantFeedbackViewModel", "cancel")
        observableIsComplete.value = true
    }

    fun sendFeedback(view: View) {
        eventLogger.log(Severity.INFO, "PlantFeedbackViewModel", "sendFeedback")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "Animal:${animal.value?.name} - " +
                    "PlantId:${plantId.value} - " +
                    "Photo: ${photo.value} - " +
                    "ScientifiName:${scientificName.value} - " +
                    "Name:${name.value} - " +
                    "Link:${link.value} - " +
                    "Text:${text.value}"
            metricsClient.log("PlantFeedbackViewModel", "Suggestion", mapOf("Data" to suggestion))
            viewModelScope.launch {
                observableIsComplete.value = true
            }
        }
    }
}
