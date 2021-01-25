package com.cesarandres.ps2link.fragments.profilepager.statlist

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.character.Day
import com.cramsan.ps2link.appcore.dbg.content.character.Month
import com.cramsan.ps2link.appcore.dbg.content.character.Stat
import com.cramsan.ps2link.appcore.dbg.content.character.Week
import com.cramsan.ps2link.appcore.getThisMonth
import com.cramsan.ps2link.appcore.getThisWeek
import com.cramsan.ps2link.appcore.getToday
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.setThisMonth
import com.cramsan.ps2link.appcore.setThisWeek
import com.cramsan.ps2link.appcore.setToday
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatListViewModel @ViewModelInject constructor(
    application: Application,
    dbgServiceClient: DBGServiceClient,
    dbgDAO: DbgDAO,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        dbgServiceClient,
        dbgDAO,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    StatListEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    private val _statList = MutableStateFlow<List<Stat>>(emptyList())
    val statList = _statList.asStateFlow()

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }

        ioScope.launch {
            val statListResponse = dbgCensus.getStatList(
                character_id = characterId,
                namespace = namespace,
                currentLang = ps2Settings.getCurrentLang() ?: CensusLang.EN,
            )?.stat_history
            _statList.value = formatStats(statListResponse)
        }
    }

    private fun formatStats(stats: List<Stat>?): List<Stat> {
        if (stats == null) {
            return emptyList()
        }

        var kills: Stat? = null
        var deaths: Stat? = null
        var score: Stat? = null
        var time: Stat? = null
        for (stat in stats) {
            if (stat.stat_name == "kills") {
                kills = stat
            } else if (stat.stat_name == "deaths") {
                deaths = stat
            } else if (stat.stat_name == "score") {
                score = stat
            } else if (stat.stat_name == "time") {
                time = stat
            }
        }
        val kdr = Stat()
        deaths?.let {
            if (deaths.all_time == "0") {
                deaths.all_time = "1"
            }
            if (deaths.getToday() == 0f) {
                deaths.setToday(1F)
            }
            if (deaths.getThisWeek() == 0f) {
                deaths.setThisWeek(1F)
            }
            if (deaths.getThisMonth() == 0f) {
                deaths.setThisMonth(1F)
            }
        }
        kdr.day = Day()
        kdr.week = Week()
        kdr.month = Month()

        kdr.stat_name = "kdr"
        kdr.all_time = (
            (kills?.all_time?.toFloatOrNull() ?: 0f) / (deaths?.all_time?.toFloatOrNull() ?: 1f)
            ).toString()

        kdr.setToday((kills?.getToday() ?: 0f) / (deaths?.getToday() ?: 1f))
        kdr.setThisWeek((kills?.getThisWeek() ?: 0f) / (deaths?.getThisWeek() ?: 1f))
        kdr.setThisMonth((kills?.getThisMonth() ?: 0f) / (deaths?.getThisMonth() ?: 1f))

        val results = mutableListOf<Stat>()
        results.add(kdr)

        val sph = Stat()
        time?.let {
            if (time.all_time == "0") {
                time.all_time = "1"
            }
            if (time.getToday() == 0f) {
                time.setToday(1F)
            }
            if (time.getThisWeek() == 0f) {
                time.setThisWeek(1F)
            }
            if (time.getThisMonth() == 0f) {
                time.setThisMonth(1F)
            }
        }
        sph.day = Day()
        sph.week = Week()
        sph.month = Month()

        sph.stat_name = "scorehour"
        sph.all_time = (
            (score?.all_time?.toFloatOrNull() ?: 0f) / ((time?.all_time?.toFloatOrNull() ?: 1f) / 3600f)
            ).toString()
        sph.setToday((score?.getToday() ?: 0f) / ((time?.getToday() ?: 3600f) / 3600f))
        sph.setThisWeek((score?.getThisWeek() ?: 0f) / ((time?.getThisWeek() ?: 3600f) / 3600f))
        sph.setThisMonth((score?.getThisMonth() ?: 0f) / ((time?.getThisMonth() ?: 3600f) / 3600f))
        results.add(sph)

        results.addAll(stats)
        return results
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }
}
