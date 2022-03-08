package com.cramsan.stranded.cardmanager.nightcards

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.models.night.CancellableByFire
import com.cramsan.stranded.lib.game.models.night.DestroyShelter
import com.cramsan.stranded.lib.game.models.night.ForageCardLost
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class NighCardManagerViewModelTest : TestBase() {

    lateinit var viewModel: NightCardManagerViewModel

    @MockK
    lateinit var cardRepository: CardRepository

    override fun setupTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { cardRepository.readNightCards() } returns listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3)

        viewModel = NightCardManagerViewModel(cardRepository, testCoroutineScope)
    }

    fun `test onShow`() = runBlockingTest {
        viewModel.onShow()

        assertEquals(3, viewModel.deck.value.size)
        assertEquals("Night1", viewModel.deck.value[0].content?.title)
        assertEquals("Night2", viewModel.deck.value[1].content?.title)
        assertEquals("Night3", viewModel.deck.value[2].content?.title)

        assertEquals("Night1", viewModel.cardTitle.value)
        assertEquals(3, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedStatementIndex.value)
        assertNull(viewModel.argument1Label.value)
        assertNull(viewModel.argument2Label.value)
    }

    fun `test changing selected card to night with no changes`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(1)

        assertEquals("Night2", viewModel.cardTitle.value)
        assertEquals(9, viewModel.cardQuantity.value)
        assertEquals(-1, viewModel.selectedStatementIndex.value)
        assertNull(viewModel.argument1Label.value)
        assertNull(viewModel.argument2Label.value)
    }

    fun `test changing selected card to night with changes that include parameters`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(2)

        assertEquals("Night3", viewModel.cardTitle.value)
        assertEquals(10, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedStatementIndex.value)
        assertEquals("Affected players", viewModel.argument1Label.value)
        assertEquals("1", viewModel.argument1Field.value)
        assertEquals("Cards to lose", viewModel.argument2Label.value)
        assertEquals("2", viewModel.argument2Field.value)
    }

    fun `test adding change statement`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(1)
        viewModel.onAddStatementSelected()

        assertEquals("Night2", viewModel.cardTitle.value)
        assertEquals(9, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedStatementIndex.value)
        assertNull(viewModel.argument1Label.value)
        assertNull(viewModel.argument2Label.value)
    }

    fun `test selecting statement type`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(2)
        viewModel.onStatementTypeIndexSelected(4)

        assertEquals("Night3", viewModel.cardTitle.value)
        assertEquals(10, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedStatementIndex.value)
        assertEquals("Only unsheltered", viewModel.argument1Label.value)
        assertEquals("true", viewModel.argument1Field.value)
        assertNull(viewModel.argument2Label.value)
        assertEquals("", viewModel.argument2Field.value)
    }

    fun `test changing statement type`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(2)
        viewModel.onStatementAtIndexSelected(1)

        assertEquals("Night3", viewModel.cardTitle.value)
        assertEquals(10, viewModel.cardQuantity.value)
        assertEquals(1, viewModel.selectedStatementIndex.value)
        assertEquals("Change", viewModel.argument1Label.value)
        assertEquals("3", viewModel.argument1Field.value)
        assertNull(viewModel.argument2Label.value)
        assertEquals("", viewModel.argument2Field.value)
    }

    fun `test saving changes`() = runBlockingTest {
        val slot = slot<List<CardHolder<NightEvent>>>()
        every { cardRepository.readNightCards() } returnsMany
            listOf(
                listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3),
                listOf(
                    CARD_HOLDER_1.copy(
                        content = NightEvent("Night2", listOf(DestroyShelter)),
                        quantity = 11,
                    ),
                    CARD_HOLDER_2, CARD_HOLDER_3
                ),
            )

        every { cardRepository.saveNightCards(capture(slot)) } returns Unit

        viewModel.onShow()
        viewModel.onQuantityTitleUpdated("11")
        viewModel.onStatementTypeIndexSelected(2)
        viewModel.onArgument1FieldUpdated("10")

        viewModel.onSaveDeckSelected()

        assertEquals("Night1", slot.captured[0].content?.title)
        assertEquals(11, slot.captured[0].quantity)

        assertEquals("Night1", viewModel.cardTitle.value)
        assertEquals(11, viewModel.cardQuantity.value)
        assertEquals(0, viewModel.selectedStatementIndex.value)
        assertEquals("Change", viewModel.argument1Label.value)
        assertEquals("10", viewModel.argument1Field.value)
        assertNull(viewModel.argument2Label.value)
    }

    companion object {
        val CARD_1 = NightEvent("Night1", listOf(CancellableByFire))
        val CARD_2 = NightEvent("Night2", listOf())
        val CARD_3 = NightEvent("Night3", listOf(ForageCardLost(2), DestroyShelter))
        val CARD_HOLDER_1 = CardHolder(CARD_1, 3)
        val CARD_HOLDER_2 = CardHolder(CARD_2, 9)
        val CARD_HOLDER_3 = CardHolder(CARD_3, 10)
    }
}
