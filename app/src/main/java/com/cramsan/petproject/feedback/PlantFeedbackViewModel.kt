package com.cramsan.petproject.feedback

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.classTag
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.appcore.model.AnimalType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantFeedbackViewModel(application: Application) : AndroidViewModel(application),
    KodeinAware {

    override val kodein by kodein(application)
    private val eventLogger: EventLoggerInterface by instance()
    private val metricsClient: MetricsInterface by instance()

    private val observableIsComplete = MutableLiveData<Boolean>()

    fun isComplete(): LiveData<Boolean> {
        return observableIsComplete
    }

    fun sendFeedback(animal: AnimalType, plantId: Long, photo: Boolean, scientificName: Boolean, name: Boolean, link: Boolean, text: String) {
        eventLogger.log(Severity.INFO, classTag(), "sendFeedback")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "Animal:${animal.name} - PlantId:$plantId - Photo: $photo - ScientifiName:$scientificName - Name:$name - Link:$link - Text:$text"
            metricsClient.log(suggestion)
            viewModelScope.launch {
                observableIsComplete.value = true
            }
        }
    }
}
