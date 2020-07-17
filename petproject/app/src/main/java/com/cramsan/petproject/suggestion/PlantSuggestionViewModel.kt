package com.cramsan.petproject.suggestion

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.logging.Severity
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.model.feedback.Feedback
import com.cramsan.petproject.appcore.model.feedback.FeedbackType
import com.cramsan.petproject.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantSuggestionViewModel(application: Application) : BaseViewModel(application) {

    val observableText = MutableLiveData<String>()
    val observableIsComplete = MutableLiveData<Boolean>()
    val observableSelectedCatToxicityRes = MutableLiveData<Int>()
    val observableSelectedDogToxicityRes = MutableLiveData<Int>()
    private val observableSelectedCatToxicity: LiveData<ToxicityValue> = Transformations.map(observableSelectedCatToxicityRes) {
            res -> when (res) {
            R.id.plant_suggestion_cat_safe -> ToxicityValue.NON_TOXIC
            R.id.plant_suggestion_cat_unsafe -> ToxicityValue.TOXIC
            else -> ToxicityValue.UNDETERMINED
        }
    }
    private val observableSelectedDogToxicity: LiveData<ToxicityValue> = Transformations.map(observableSelectedDogToxicityRes) {
            res -> when (res) {
            R.id.plant_suggestion_cat_safe -> ToxicityValue.TOXIC
            R.id.plant_suggestion_cat_unsafe -> ToxicityValue.NON_TOXIC
            else -> ToxicityValue.UNDETERMINED
        }
    }

    override val logTag: String
        get() = "PlantSuggestionViewModel"

    fun cancel(view: View) {
        observableIsComplete.value = true
    }

    fun save(view: View) {
        eventLogger.log(Severity.INFO, "PlantSuggestionViewModel", "savePlant")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion = "${observableText.value}: Cats:${observableSelectedCatToxicity.value?.name} - Dogs:${observableSelectedDogToxicity.value?.name}"
            val feedback = Feedback(-1, FeedbackType.NEW_PLANT, suggestion, -1)
            metricsClient.log("PlantSuggestionViewModel", "suggestion", mapOf("Data" to suggestion))
            viewModelScope.launch {
                observableIsComplete.value = true
            }
        }
    }
}
