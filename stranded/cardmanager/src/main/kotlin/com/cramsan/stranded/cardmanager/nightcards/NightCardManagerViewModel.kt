package com.cramsan.stranded.cardmanager.nightcards

import com.cramsan.stranded.cardmanager.base.BaseCardManagerViewModel
import com.cramsan.stranded.lib.game.models.night.CancellableByFire
import com.cramsan.stranded.lib.game.models.night.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.night.DamageToDo
import com.cramsan.stranded.lib.game.models.night.DestroyShelter
import com.cramsan.stranded.lib.game.models.night.FiberLost
import com.cramsan.stranded.lib.game.models.night.FireUnavailableTomorrow
import com.cramsan.stranded.lib.game.models.night.ForageCardLost
import com.cramsan.stranded.lib.game.models.night.NightChangeStatement
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.night.SelectTargetOnlyUnsheltered
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantity
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.night.Survived
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NightCardManagerViewModel(
    cardRepository: CardRepository,
    scope: CoroutineScope,
) : BaseCardManagerViewModel<NightEvent>(cardRepository, scope) {

    override val tabTitle = "Night Cards"

    private val _selectedStatementIndex = MutableStateFlow(-1)
    val selectedStatementIndex: StateFlow<Int> = _selectedStatementIndex

    private val _statementList = MutableStateFlow<List<NightChangeStatement>>(listOf())
    val statementList: StateFlow<List<NightChangeStatement>> = _statementList

    private val _statement = MutableStateFlow<NightChangeStatement?>(null)
    val statement: StateFlow<NightChangeStatement?> = _statement

    private val _argument1Label = MutableStateFlow<String?>(null)
    val argument1Label: StateFlow<String?> = _argument1Label

    private val _argument1Field = MutableStateFlow("")
    val argument1Field: StateFlow<String> = _argument1Field

    private val _argument2Label = MutableStateFlow<String?>(null)
    val argument2Label: StateFlow<String?> = _argument2Label

    private val _argument2Field = MutableStateFlow("")
    val argument2Field: StateFlow<String> = _argument2Field

    private var selectedStatementTypeIndex = -1

    private val onSelectedStatementIndexChange = callback@{
        val statement = _statementList.value.elementAtOrNull(_selectedStatementIndex.value)
        _statement.value = statement

        if (_selectedStatementIndex.value < 0) {
            return@callback
        }

        if (statement == null) {
            _statementList.value = _statementList.value + CancellableByFire
        }

        loadStatementAtIndex(_selectedStatementIndex.value)
    }

    override fun onShow(): Job {
        val job = super.onShow()
        _selectedStatementIndex.onEach {
            onSelectedStatementIndexChange()
        }.launchIn(scope)
        return job
    }

    override fun readDeckFromRepository(): List<CardHolder<NightEvent>> {
        return cardRepository.readNightCards()
    }

    override fun writeDeckToRepository(deck: List<CardHolder<NightEvent>>) {
        cardRepository.saveNightCards(deck)
    }

    override fun instanciateNewCard(): NightEvent {
        val newStatementList = instantiateStatementList()

        return NightEvent(
            cardTitle.value,
            newStatementList,
        )
    }

    private fun instantiateStatementList(): List<NightChangeStatement> {
        val sanitizedStatement = when (val statementToSanitize = _statement.value) {
            CancellableByFire, DestroyShelter, FireUnavailableTomorrow, SelectTargetQuantityAll,
            FiberLost, Survived -> statementToSanitize
            is SelectTargetOnlyUnsheltered -> {
                statementToSanitize.copy(
                    onlyUnsheltered = _argument1Field.value.toBooleanStrictOrNull() ?: false
                )
            }
            is SelectTargetQuantity -> {
                statementToSanitize.copy(
                    affectedPlayers = _argument1Field.value.toIntOrNull() ?: 0
                )
            }
            is CancellableByWeapon -> {
                statementToSanitize.copy(
                    change = _argument1Field.value.toIntOrNull() ?: 0,
                    damage = _argument2Field.value.toIntOrNull() ?: 0,
                )
            }
            is ForageCardLost -> {
                statementToSanitize.copy(
                    cardsLost = _argument1Field.value.toIntOrNull() ?: 0,
                )
            }
            is DamageToDo -> {
                statementToSanitize.copy(
                    healthChange = _argument1Field.value.toIntOrNull() ?: 0
                )
            }
            null -> null
        } ?: return emptyList()

        val newList = statementList.value.toMutableList()
        newList[selectedStatementIndex.value] = sanitizedStatement
        return newList
    }

    override fun loadCardAtIndex(index: Int) {
        val selectedCard = deck.value[selectedCardIndex.value].content
        selectedCard?.statements.let {
            if (it == null) {
                _statementList.value = emptyList()
            } else {
                _statementList.value = it
            }
            if (it.isNullOrEmpty()) {
                _selectedStatementIndex.value = -1
                selectedStatementTypeIndex = -1
            } else {
                _selectedStatementIndex.value = 0
                selectedStatementTypeIndex = 0
            }
        }
        onSelectedStatementIndexChange()
    }

    fun onStatementAtIndexSelected(index: Int) {
        saveCardToDeck()
        _selectedStatementIndex.value = index
    }

    fun onAddStatementSelected() {
        saveCardToDeck()
        _selectedStatementIndex.value = _statementList.value.size
    }

    fun onRemoveStatementSelected() {
        val newStatementList = _statementList.value.toMutableList()
        if (newStatementList.isEmpty()) {
            return
        }
        newStatementList.removeAt(selectedStatementIndex.value)
        _statementList.value = newStatementList
        if (newStatementList.isEmpty() || selectedStatementIndex.value > _statementList.value.lastIndex) {
            _selectedStatementIndex.value = selectedStatementIndex.value - 1
        } else {
            onSelectedStatementIndexChange()
        }
    }

    private fun loadStatementAtIndex(index: Int) {
        _argument1Label.value = null
        _argument1Field.value = ""
        _argument2Label.value = null
        _argument2Field.value = ""

        val statement = _statementList.value[index]
        _statement.value = statement
        when (statement) {
            CancellableByFire, DestroyShelter, FireUnavailableTomorrow, SelectTargetQuantityAll,
            FiberLost, Survived -> Unit
            is SelectTargetOnlyUnsheltered -> {
                _argument1Label.value = "Only unsheltered"
                _argument1Field.value = statement.onlyUnsheltered.toString()
            }
            is SelectTargetQuantity -> {
                _argument1Label.value = "Affected players"
                _argument1Field.value = statement.affectedPlayers.toString()
            }
            is CancellableByWeapon -> {
                _argument1Label.value = "Change"
                _argument1Field.value = statement.change.toString()
                _argument2Label.value = "Damage"
                _argument2Field.value = statement.damage.toString()
            }
            is ForageCardLost -> {
                _argument2Label.value = "Cards to lose"
                _argument2Field.value = statement.cardsLost.toString()
            }
            is DamageToDo -> {
                _argument1Label.value = "Change"
                _argument1Field.value = statement.healthChange.toString()
            }
        }
    }

    private fun instantiateNewStatement(): NightChangeStatement {
        return when (STATEMENT_TYPES[selectedStatementTypeIndex]) {
            STATEMENT_TYPES[0] -> CancellableByFire
            STATEMENT_TYPES[1] -> DestroyShelter
            STATEMENT_TYPES[2] -> FireUnavailableTomorrow
            STATEMENT_TYPES[3] -> SelectTargetOnlyUnsheltered(true)
            STATEMENT_TYPES[4] -> SelectTargetQuantity(1)
            STATEMENT_TYPES[5] -> SelectTargetQuantityAll
            STATEMENT_TYPES[6] -> CancellableByWeapon(1, 3)
            STATEMENT_TYPES[7] -> ForageCardLost(1)
            STATEMENT_TYPES[8] -> FiberLost
            STATEMENT_TYPES[9] -> DamageToDo(1)
            STATEMENT_TYPES[10] -> Survived
            else -> TODO()
        }
    }

    fun onStatementTypeIndexSelected(index: Int) {
        selectedStatementTypeIndex = index
        val newStatement = instantiateNewStatement()
        val list = _statementList.value.toMutableList()
        list[_selectedStatementIndex.value] = newStatement
        _statementList.value = list

        saveCardToDeck()
        onSelectedStatementIndexChange()
    }

    fun onArgument1FieldUpdated(argument: String) {
        _argument1Field.value = argument
    }

    fun onArgument2FieldUpdated(argument: String) {
        _argument1Field.value = argument
    }

    companion object {
        val STATEMENT_TYPES = listOf(
            "CancellableByFire",
            "DestroyShelter",
            "FireUnavailableTomorrow",
            "SelectTargetOnlyUnsheltered",
            "SelectTargetQuantity",
            "SelectTargetQuantityAll",
            "CancellableByWeapon",
            "ForageCardLost",
            "FiberLost",
            "DamageToDo",
            "Survived",
        )
    }
}
