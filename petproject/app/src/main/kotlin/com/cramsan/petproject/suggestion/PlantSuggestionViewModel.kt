package com.cramsan.petproject.suggestion

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.LiveEvent
import com.cramsan.framework.logging.logI
import com.cramsan.framework.userevents.logEvent
import com.cramsan.petproject.R
import com.cramsan.petproject.appcore.model.ToxicityValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantSuggestionViewModel @Inject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle) {

    val observableText = MutableLiveData<String>()
    val observableIsComplete = LiveEvent<CompletedEvent>()
    val observableSelectedCatToxicityRes = MutableLiveData<Int>()
    val observableSelectedDogToxicityRes = MutableLiveData<Int>()

    private val observableSelectedCatToxicity: LiveData<ToxicityValue> =
        Transformations.map(observableSelectedCatToxicityRes) { res ->
            when (res) {
                R.id.plant_suggestion_cat_safe -> ToxicityValue.NON_TOXIC
                R.id.plant_suggestion_cat_unsafe -> ToxicityValue.TOXIC
                else -> ToxicityValue.UNDETERMINED
            }
        }
    private val observableSelectedDogToxicity: LiveData<ToxicityValue> =
        Transformations.map(observableSelectedDogToxicityRes) { res ->
            when (res) {
                R.id.plant_suggestion_cat_safe -> ToxicityValue.TOXIC
                R.id.plant_suggestion_cat_unsafe -> ToxicityValue.NON_TOXIC
                else -> ToxicityValue.UNDETERMINED
            }
        }

    override val logTag: String
        get() = "PlantSuggestionViewModel"

    fun cancel(@Suppress("UNUSED_PARAMETER") view: View) {
        observableIsComplete.value = CompletedEvent(false)
    }

    fun save(@Suppress("UNUSED_PARAMETER") view: View) {
        logI("PlantSuggestionViewModel", "savePlant")
        viewModelScope.launch(Dispatchers.IO) {
            val suggestion =
                "${observableText.value}: Cats:${observableSelectedCatToxicity.value?.name} - Dogs:${observableSelectedDogToxicity.value?.name}"
            // val feedback = Feedback(-1, FeedbackType.NEW_PLANT, suggestion, -1)
            logEvent("PlantSuggestionViewModel", "suggestion", mapOf("Data" to suggestion))
            viewModelScope.launch {
                observableIsComplete.value = CompletedEvent(true)
            }
        }
    }
}
