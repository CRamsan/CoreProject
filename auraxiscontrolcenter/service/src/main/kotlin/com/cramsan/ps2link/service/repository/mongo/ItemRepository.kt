package com.cramsan.ps2link.service.repository.mongo

import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.repository.mongo.models.Item
import kotlinx.datetime.Clock
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.upsert

/**
 * Class for our articles database queries
 */
class ItemRepository(
    private val items: CoroutineCollection<Item>,
    private val clock: Clock,
) {
    /**
     * Query to create a new article
     */
    suspend fun createItem(item: Item): Item? {
        logI(TAG, "Create Item ${item.itemId}")
        val toBeInserted = item.setLastUpdateField()

        return if (items.insertOne(toBeInserted).wasAcknowledged()) {
            toBeInserted
        } else {
            null
        }
    }

    /**
     * Query for an article by ID
     */
    suspend fun getItem(itemId: String, namespace: Namespace): Item? {
        logI(TAG, "Get Item $itemId")
        return items.findOne(
            Item::itemId eq itemId,
            Item::namespace eq namespace,
        )
    }

    /**
     * Query to update an existing article, if it exists. False returned if
     * article not found with the ID
     */
    suspend fun updateItem(item: Item): Item? {
        logI(TAG, "Update Item ${item.itemId}")
        val toBeInserted = item.setLastUpdateField()

        return if (items.updateOne(
                and(
                        Item::itemId eq item.itemId,
                        Item::namespace eq item.namespace,
                    ),
                toBeInserted,
                options = upsert()
            ).wasAcknowledged()
        ) {
            toBeInserted
        } else {
            null
        }
    }

    /**
     * Delete article from the db if the ID is found.
     */
    suspend fun deleteItem(itemId: String, namespace: Namespace): Boolean {
        logI(TAG, "Delete Item $itemId")
        return items.deleteOne(
            and(
                Item::itemId eq itemId,
                Item::namespace eq namespace,
            )
        ).wasAcknowledged()
    }

    private fun Item.setLastUpdateField(): Item {
        return copy(lastUpdated = clock.now().epochSeconds)
    }

    companion object {
        private const val TAG = "ItemRepository"
    }
}
