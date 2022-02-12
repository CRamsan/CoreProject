package com.cramsan.stranded.cardmanager.foragecards

import com.cramsan.framework.test.TestBase
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.game.models.scavenge.Consumable
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.scavenge.Useless
import com.cramsan.stranded.lib.storage.CardHolder
import com.cramsan.stranded.lib.storage.CardRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ForageCardManagerViewModelTest : TestBase() {

    lateinit var viewModel: ForageCardManagerViewModel

    @MockK
    lateinit var cardRepository: CardRepository

    override fun setupTest() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { cardRepository.readForageCards() } returns listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3)

        viewModel = ForageCardManagerViewModel(cardRepository, testCoroutineScope)
    }

    @Test
    fun `test onShow`() = runBlockingTest {
        viewModel.onShow()

        assertEquals(3, viewModel.deck.value.size)
        assertEquals("Item1", viewModel.deck.value[0].content?.title)
        assertEquals("Card2", viewModel.deck.value[1].content?.title)
        assertEquals("STICK", viewModel.deck.value[2].content?.title)

        assertEquals("Item1", viewModel.cardTitle.value)
        assertEquals(2, viewModel.cardQuantity.value)
        assertEquals("Useless", viewModel.cardType.value)
        assertNull(viewModel.remainingUses.value)
        assertNull(viewModel.healthModifier.value)
        assertNull(viewModel.remainingDays.value)
    }

    @Test
    fun `test changing selected card to consumable`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(1)

        assertEquals("Card2", viewModel.cardTitle.value)
        assertEquals(4, viewModel.cardQuantity.value)
        assertEquals("Consumable", viewModel.cardType.value)
        assertEquals(3, viewModel.remainingUses.value)
        assertEquals(2, viewModel.healthModifier.value)
        assertEquals(1, viewModel.remainingDays.value)
    }

    @Test
    fun `test changing selected card to resource`() = runBlockingTest {
        viewModel.onShow()
        viewModel.onCardAtIndexSelected(2)

        assertEquals("STICK", viewModel.cardTitle.value)
        assertEquals(6, viewModel.cardQuantity.value)
        assertEquals("Resource", viewModel.cardType.value)
        assertNull(viewModel.remainingUses.value)
        assertNull(viewModel.healthModifier.value)
        assertNull(viewModel.remainingDays.value)
    }

    @Test
    fun `test saving changes`() = runBlockingTest {

        val slot = slot<List<CardHolder<ScavengeResult>>>()

        val updatedTitle = "FIBER"
        every { cardRepository.readForageCards() } returnsMany
            listOf(
                listOf(CARD_HOLDER_1, CARD_HOLDER_2, CARD_HOLDER_3),
                listOf(
                    CARD_HOLDER_1.copy(
                        content = Resource(ResourceType.FIBER),
                        quantity = 11,
                    ),
                    CARD_HOLDER_2, CARD_HOLDER_3
                ),
            )

        every { cardRepository.saveForageCards(capture(slot)) } returns Unit

        viewModel.onShow()
        viewModel.onQuantityTitleUpdated("11")
        viewModel.setCardType("Resource")
        viewModel.setResourceType(ResourceType.FIBER)

        viewModel.onSaveDeckSelected()

        assertEquals(updatedTitle, slot.captured[0].content?.title)
        assertEquals(11, slot.captured[0].quantity)

        assertEquals(updatedTitle, viewModel.deck.value[0].content?.title)
        assertEquals(updatedTitle, viewModel.cardTitle.value)
        assertEquals(11, viewModel.cardQuantity.value)
        assertEquals("Resource", viewModel.cardType.value)
    }

    companion object {
        val CARD_1 = Useless("Item1")
        val CARD_2 = Consumable("Card2", 1, 2, Status.NORMAL, 3)
        val CARD_3 = Resource(ResourceType.STICK)
        val CARD_HOLDER_1 = CardHolder<ScavengeResult>(CARD_1, 2)
        val CARD_HOLDER_2 = CardHolder<ScavengeResult>(CARD_2, 4)
        val CARD_HOLDER_3 = CardHolder<ScavengeResult>(CARD_3, 6)
    }
}
