package com.cramsan.stranded.cardmanager

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BaseCardManagerViewModelTest : TestBase() {

    lateinit var viewModel: BaseCardManagerViewModel<NightEvent>

    @MockK
    lateinit var cardRepository: CardRepository

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { cardRepository.readNightCards() } returns listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3)

        viewModel = NightCardManagerViewModel(cardRepository, testCoroutineScope)
    }

    @Test
    fun `test initial state`() = runBlockingTest {
        assertEquals("New title", viewModel.cardTitle.value)
        assertEquals(0, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedCardIndex.value)
        assertEquals(listOf(CardHolder<NightEvent>(null, 0)), viewModel.deck.value)
    }

    @Test
    fun `test onShow`() = runBlockingTest {
        viewModel.onShow()

        assertEquals(3, viewModel.deck.value.size)
        assertEquals("Card1", viewModel.cardTitle.value)
        assertEquals(2, viewModel.cardQuantity.value)
        assertEquals("Card1", viewModel.deck.value[0].content?.title)
        assertEquals("Card2", viewModel.deck.value[1].content?.title)
        assertEquals("Card3", viewModel.deck.value[2].content?.title)
    }

    @Test
    fun `test initializing empty deck`() = runBlockingTest {
        every { cardRepository.readNightCards() } returns emptyList()

        viewModel.onShow()

        assertEquals(1, viewModel.deck.value.size)
        assertNull(viewModel.deck.value[0].content?.title)
        assertEquals(0, viewModel.deck.value[0].quantity)
    }

    @Test
    fun `test changing selected card`() = runBlockingTest {
        viewModel.onShow()
        viewModel.selectedCardAtIndex(2)

        assertEquals("Card3", viewModel.cardTitle.value)
        assertEquals(6, viewModel.cardQuantity.value)
    }

    @Test
    fun `test adding new card`() = runBlockingTest {
        viewModel.onShow()
        viewModel.newCard()

        assertEquals(4, viewModel.deck.value.size)
        assertNull(viewModel.deck.value[3].content?.title)
        assertEquals("New title", viewModel.cardTitle.value)
        assertEquals(0, viewModel.cardQuantity.value)
    }

    @Test
    fun `test removing card`() = runBlockingTest {
        viewModel.onShow()
        viewModel.removeCard()

        assertEquals(2, viewModel.deck.value.size)
        assertEquals("Card2", viewModel.deck.value[0].content?.title)
        assertEquals("Card2", viewModel.cardTitle.value)
        assertEquals(4, viewModel.cardQuantity.value)
    }

    @Test
    fun `test removing last card`() = runBlockingTest {
        every { cardRepository.readNightCards() } returns emptyList()

        viewModel.onShow()
        viewModel.removeCard()
        viewModel.removeCard()

        assertEquals(1, viewModel.deck.value.size)
        assertNull(viewModel.deck.value[0].content?.title)
        assertEquals(0, viewModel.deck.value[0].quantity)
    }

    @Test
    fun `test saving changes`() = runBlockingTest {
        val slot = slot<List<CardHolder<NightEvent>>>()

        val updatedTitle = "Updated Card Title"
        every { cardRepository.readNightCards() } returnsMany
            listOf(
                listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3),
                listOf(
                    CARD_HOLDER_1.copy(
                        content = NightEvent(updatedTitle, emptyList()),
                        quantity = 10,
                    ),
                    CARD_HOLDER_2, CARD_HOLDER_3
                ),
            )

        every { cardRepository.saveNightCards(capture(slot)) } returns Unit

        viewModel.onShow()
        viewModel.updateTitle(updatedTitle)
        viewModel.updateQuantity("10")

        viewModel.saveDeck()

        assertEquals(updatedTitle, slot.captured[0].content?.title)
        assertEquals(10, slot.captured[0].quantity)
        assertEquals(updatedTitle, viewModel.deck.value[0].content?.title)
        assertEquals(updatedTitle, viewModel.cardTitle.value)
        assertEquals(10, viewModel.cardQuantity.value)
    }

    @Test
    fun `test changing card and then creating a new card`() = runBlockingTest {
        val updatedTitle = "Updated Card Title"

        viewModel.onShow()
        viewModel.updateTitle(updatedTitle)
        viewModel.updateQuantity("10")
        viewModel.newCard()

        assertEquals(updatedTitle, viewModel.deck.value[0].content?.title)
    }

    companion object {
        val CARD_1 = NightEvent("Card1", emptyList())
        val CARD_2 = NightEvent("Card2", emptyList())
        val CARD_3 = NightEvent("Card3", emptyList())
        val CARD_HOLDER_1 = CardHolder(CARD_1, 2)
        val CARD_HOLDER_2 = CardHolder(CARD_2, 4)
        val CARD_HOLDER_3 = CardHolder(CARD_3, 6)
    }
}
