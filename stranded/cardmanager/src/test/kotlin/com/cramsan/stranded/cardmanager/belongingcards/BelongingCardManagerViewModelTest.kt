package com.cramsan.stranded.cardmanager.belongingcards

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Equippable
import com.cramsan.stranded.lib.game.models.common.StartingFood
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BelongingCardManagerViewModelTest : TestBase() {

    lateinit var viewModel: BelongingCardManagerViewModel

    @MockK
    lateinit var cardRepository: CardRepository

    override fun setupTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { cardRepository.readBelongingCards() } returns listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3, CARD_HOLDER_4)

        viewModel = BelongingCardManagerViewModel(cardRepository, testCoroutineScope)
    }

    @Test
    fun `test onShow`() = runBlockingTest {
        viewModel.onShow()

        assertEquals(4, viewModel.deck.value.size)
        assertEquals("Card1", viewModel.deck.value[0].content?.title)
        assertEquals("Card2", viewModel.deck.value[1].content?.title)
        assertEquals("Card3", viewModel.deck.value[2].content?.title)
        assertEquals("Card4", viewModel.deck.value[3].content?.title)

        assertEquals("Card1", viewModel.cardTitle.value)
        assertEquals(2, viewModel.cardQuantity.value)
        assertEquals("Equippable", viewModel.cardType.value)
        assertEquals(3, viewModel.remainingUses.value)
        assertNull(viewModel.healthModifier.value)
        assertNull(viewModel.remainingDays.value)
    }

    @Test
    fun `test changing selected card to starting food`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(3)

        assertEquals("Card4", viewModel.cardTitle.value)
        assertEquals(5, viewModel.cardQuantity.value)
        assertEquals("Starting Food", viewModel.cardType.value)
        assertEquals(1, viewModel.remainingUses.value)
        assertEquals(3, viewModel.healthModifier.value)
        assertEquals(2, viewModel.remainingDays.value)
    }

    @Test
    fun `test changing selected card to equippable`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(2)

        assertEquals("Card3", viewModel.cardTitle.value)
        assertEquals(6, viewModel.cardQuantity.value)
        assertEquals("Equippable", viewModel.cardType.value)
        assertEquals(4, viewModel.remainingUses.value)
        assertNull(viewModel.healthModifier.value)
        assertNull(viewModel.remainingDays.value)
    }

    @Test
    fun `test saving changes`() = runBlockingTest {
        val slot = slot<List<CardHolder<Belongings>>>()

        val updatedTitle = "Updated Card Title"
        every { cardRepository.readBelongingCards() } returnsMany
            listOf(
                listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3, CARD_HOLDER_4),
                listOf(
                    CARD_HOLDER_1.copy(
                        content = StartingFood(updatedTitle, 2, 3, Status.NORMAL, 4),
                        quantity = 10,
                    ),
                    CARD_HOLDER_2, CARD_HOLDER_3, CARD_HOLDER_4
                ),
            )

        every { cardRepository.saveBelongingCards(capture(slot)) } returns Unit

        viewModel.onShow()
        viewModel.onTitleFieldUpdated(updatedTitle)
        viewModel.onQuantityTitleUpdated("10")
        viewModel.setCardType("Starting Food")

        viewModel.onSaveDeckSelected()

        assertEquals(updatedTitle, slot.captured[0].content?.title)
        assertEquals(10, slot.captured[0].quantity)
        assertTrue(slot.captured[0].content is StartingFood)

        assertEquals(updatedTitle, viewModel.deck.value[0].content?.title)
        assertEquals(updatedTitle, viewModel.cardTitle.value)
        assertEquals(10, viewModel.cardQuantity.value)
        assertEquals("Starting Food", viewModel.cardType.value)
    }

    companion object {
        val CARD_1 = Equippable("Card1", 3)
        val CARD_2 = StartingFood("Card2", 1, 2, Status.NORMAL, 3)
        val CARD_3 = Equippable("Card3", 4)
        val CARD_4 = StartingFood("Card4", 2, 3, Status.NORMAL, 1)
        val CARD_HOLDER_1 = CardHolder<Belongings>(CARD_1, 2)
        val CARD_HOLDER_2 = CardHolder<Belongings>(CARD_2, 4)
        val CARD_HOLDER_3 = CardHolder<Belongings>(CARD_3, 6)
        val CARD_HOLDER_4 = CardHolder<Belongings>(CARD_4, 5)
    }
}
