package com.cramsan.petproject.suggestion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity

import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.model.feedback.Feedback
import com.cramsan.petproject.appcore.model.feedback.FeedbackType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.erased.instance

class PlantSuggestionViewModel(application: Application) : AndroidViewModel(application),
    KodeinAware {

    override val kodein by kodein(application)
    private val eventLogger: EventLoggerInterface by instance()
    private val metricsClient: MetricsInterface by instance()

    private val observableIsComplete = MutableLiveData<Boolean>()

    fun isComplete(): LiveData<Boolean> {
        return observableIsComplete
    }

    fun savePlant(mainName: String, toxicityForCats: ToxicityValue, toxicityForDogs: ToxicityValue) {
        eventLogger.log(Severity.INFO, "PlantSuggestionViewModel", "savePlant")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "$mainName: Cats:${toxicityForCats.name} - Dogs:${toxicityForDogs.name}"
            val feedback = Feedback(-1, FeedbackType.NEW_PLANT, suggestion, -1)
            metricsClient.log("PlantSuggestionViewModel", suggestion)
            viewModelScope.launch {
                observableIsComplete.value = true
            }
        }
    }
}
