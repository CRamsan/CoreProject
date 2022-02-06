package com.cramsan.stranded.cardmanager

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.stranded.cardmanager.base.CardManager
import com.cramsan.stranded.cardmanager.belongingcards.BelongingCardManagerViewModel
import com.cramsan.stranded.cardmanager.belongingcards.BelongingsCardsTab
import com.cramsan.stranded.cardmanager.foragecards.ForageCardManagerViewModel
import com.cramsan.stranded.cardmanager.foragecards.ForageCardsTab
import com.cramsan.stranded.cardmanager.nightcards.NightCardManagerViewModel
import com.cramsan.stranded.cardmanager.nightcards.NightCardsTab
import com.cramsan.stranded.lib.messages.module
import com.cramsan.stranded.lib.storage.FileBasedCardRepository
import kotlinx.coroutines.MainScope
import kotlinx.serialization.json.Json

fun main() = application {
    val cardRepository = FileBasedCardRepository(
        filename = "test.json",
        json = Json {
            serializersModule = module
            prettyPrint = true
        }
    )

    val scope = MainScope()

    val belongingViewModel = BelongingCardManagerViewModel(cardRepository, scope)
    val forageViewModel = ForageCardManagerViewModel(cardRepository, scope)
    val nightViewModel = NightCardManagerViewModel(cardRepository, scope)

    val titles = listOf(nightViewModel, forageViewModel, belongingViewModel).map { it.tabTitle }

    Window(onCloseRequest = ::exitApplication) {
        CardManager(titles) { index ->
            when (index) {
                0 -> NightCardsTab(nightViewModel)
                1 -> ForageCardsTab(forageViewModel)
                2 -> BelongingsCardsTab(belongingViewModel)
            }
        }
    }

    cardRepository.initialize()
    forageViewModel.onShow()
    belongingViewModel.onShow()
    nightViewModel.onShow()
}
