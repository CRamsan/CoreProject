package com.cramsan.stranded.cardmanager.nightcards

import com.cramsan.stranded.cardmanager.base.BaseCardManagerViewModel
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.state.CancellableByFire
import com.cramsan.stranded.lib.game.models.state.CancellableByFood
import com.cramsan.stranded.lib.game.models.state.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.state.Change
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DamageToDo
import com.cramsan.stranded.lib.game.models.state.DestroyShelter
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.FiberLost
import com.cramsan.stranded.lib.game.models.state.FireModification
import com.cramsan.stranded.lib.game.models.state.FireUnavailableTomorrow
import com.cramsan.stranded.lib.game.models.state.ForageCardLost
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.MultiHealthChange
import com.cramsan.stranded.lib.game.models.state.SelectTargetOnlyUnsheltered
import com.cramsan.stranded.lib.game.models.state.SelectTargetQuantity
import com.cramsan.stranded.lib.game.models.state.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.Survived
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NightCardManagerViewModel(
    cardRepository: CardRepository,
    scope: CoroutineScope,
) : BaseCardManagerViewModel<NightEvent>(cardRepository, scope) {

    override val tabTitle = "Night Cards"

    private val _selectedChangeIndex = MutableStateFlow(-1)
    val selectedChangeIndex: StateFlow<Int> = _selectedChangeIndex

    protected val _changeList = MutableStateFlow<List<Change>>(listOf())
    val changeList: StateFlow<List<Change>> = _changeList

    protected val _change = MutableStateFlow<Change?>(null)
    val change: StateFlow<Change?> = _change

    private val _argument1Label = MutableStateFlow<String?>(null)
    val argument1Label: StateFlow<String?> = _argument1Label

    private val _argument1Field = MutableStateFlow<String>("")
    val argument1Field: StateFlow<String> = _argument1Field

    private val _argument2Label = MutableStateFlow<String?>(null)
    val argument2Label: StateFlow<String?> = _argument2Label

    private val _argument2Field = MutableStateFlow<String>("")
    val argument2Field: StateFlow<String> = _argument2Field

    private val _changeType = MutableStateFlow(CHANGE_TYPES.first())
    val changeType: StateFlow<String> = _changeType

    private val onSelectedChangeIndexChange = callback@{
        val change = _changeList.value.elementAtOrNull(_selectedChangeIndex.value)
        _change.value = change

        if (_selectedChangeIndex.value < 0) {
            return@callback
        }

        if (change == null) {
            _changeList.value = _changeList.value + CancellableByFire
        }

        loadChangeAtIndex(_selectedChangeIndex.value)
    }

    init {
        _selectedChangeIndex.onEach {
            onSelectedChangeIndexChange()
        }.launchIn(scope)
    }

    override fun readDeckFromRepository(): List<CardHolder<NightEvent>> {
        return cardRepository.readNightCards()
    }

    override fun writeDeckToRepository(deck: List<CardHolder<NightEvent>>) {
        cardRepository.saveNightCards(deck)
    }

    override fun instanciateNewCard(): NightEvent {
        return NightEvent(
            cardTitle.value,
            changeList.value,
        )
    }

    override fun loadCardAtIndex(index: Int) = Unit

    override fun sanitizeInput() = Unit

    fun onChangeAtIndexSelected(index: Int) {
        saveCardToDeck()
        _selectedChangeIndex.value = index
    }

    fun onAddChangeStatementSelected() {
        saveCardToDeck()
        _selectedChangeIndex.value = _changeList.value.size
    }

    fun onRemoveChangeStatementSelected() {
        val newChangeList = _changeList.value.toMutableList()
        newChangeList.removeAt(selectedChangeIndex.value)
        _changeList.value = newChangeList
        if (_changeList.value.isNotEmpty() && selectedChangeIndex.value > _changeList.value.lastIndex) {
            _selectedChangeIndex.value = selectedChangeIndex.value - 1
        } else {
            onSelectedChangeIndexChange()
        }
    }

    private fun loadChangeAtIndex(index: Int) {
        _argument1Label.value = null
        _argument1Field.value = ""
        _argument2Label.value = null
        _argument2Field.value = ""

        val change = _changeList.value.elementAt(index)

        _changeType.value = when (change) {
            is CancellableByFire -> CHANGE_TYPES[0]
            is DestroyShelter -> CHANGE_TYPES[1]
            is CancellableByFood -> CHANGE_TYPES[2]
            is FireUnavailableTomorrow -> CHANGE_TYPES[3]
            is SelectTargetOnlyUnsheltered -> CHANGE_TYPES[4]
            is SelectTargetQuantity -> CHANGE_TYPES[5]
            is SelectTargetQuantityAll -> CHANGE_TYPES[6]
            is CancellableByWeapon -> CHANGE_TYPES[7]
            is ForageCardLost -> CHANGE_TYPES[8]
            is FiberLost -> CHANGE_TYPES[9]
            is FireModification -> CHANGE_TYPES[10]
            is DamageToDo -> CHANGE_TYPES[11]
            is Survived -> CHANGE_TYPES[12]
            else -> TODO()
        }

        when (change) {
            CancellableByFire, DestroyShelter, FireUnavailableTomorrow, SelectTargetQuantityAll,
            FiberLost, Survived -> Unit
            is CancellableByFood -> {
                _argument1Label.value = "Change"
                _argument1Field.value = change.change.toString()
            }
            is SelectTargetOnlyUnsheltered -> {
                _argument1Label.value = "Only unsheltered"
                _argument1Field.value = change.onlyUnsheltered.toString()
            }
            is SelectTargetQuantity -> {
                _argument1Label.value = "Affected players"
                _argument1Field.value = change.affectedPlayers.toString()
            }
            is CancellableByWeapon -> {
                _argument1Label.value = "Change"
                _argument1Field.value = change.change.toString()
            }
            is ForageCardLost -> {
                _argument1Label.value = "Affected players"
                _argument1Field.value = change.affectedPlayers.toString()
                _argument1Label.value = "Cards to lose"
                _argument1Field.value = change.cardsLost.toString()
            }
            is FireModification -> {
                _argument1Label.value = "Change"
                _argument1Field.value = change.change.toString()
            }
            is DamageToDo -> {
                _argument1Label.value = "Change"
                _argument1Field.value = change.healthChange.toString()
            }

            is SingleHealthChange, is MultiHealthChange, is CraftCard, is DrawBelongingCard, DrawNightCard,
            is DrawScavengeCard, IncrementNight, is SetPhase, is UserCard -> Unit
        }
    }

    private fun instantiateNewChange(): Change {
        return when (_changeType.value) {
            CHANGE_TYPES[0] -> CancellableByFire
            CHANGE_TYPES[1] -> DestroyShelter
            CHANGE_TYPES[2] -> CancellableByFood(1)
            CHANGE_TYPES[3] -> FireUnavailableTomorrow
            CHANGE_TYPES[4] -> SelectTargetOnlyUnsheltered(true)
            CHANGE_TYPES[5] -> SelectTargetQuantity(1)
            CHANGE_TYPES[6] -> SelectTargetQuantityAll
            CHANGE_TYPES[7] -> CancellableByWeapon(1)
            CHANGE_TYPES[8] -> ForageCardLost(1, 1)
            CHANGE_TYPES[9] -> FiberLost
            CHANGE_TYPES[10] -> FireModification(1)
            CHANGE_TYPES[11] -> DamageToDo(1)
            CHANGE_TYPES[12] -> Survived
            else -> TODO()
        }
    }

    fun onChangeTypeSelected(type: String) {
        _changeType.value = type

        val newChange = instantiateNewChange()
        val list = _changeList.value.toMutableList()
        list[_selectedChangeIndex.value] = newChange
        _changeList.value = list

        saveCardToDeck()
        onSelectedChangeIndexChange()
    }

    companion object {
        val CHANGE_TYPES = listOf(
            "CancellableByFire",
            "DestroyShelter",
            "CancellableByFood",
            "FireUnavailableTomorrow",
            "SelectTargetOnlyUnsheltered",
            "SelectTargetQuantity",
            "SelectTargetQuantityAll",
            "CancellableByWeapon",
            "ForageCardLost",
            "FiberLost",
            "FireModification",
            "DamageToDo",
            "Survived",
        )
    }
}
