package com.cramsan.awslib.dsl

import com.cramsan.awslib.dsl.builders.ConsumableItemBuilder
import com.cramsan.awslib.dsl.builders.EntityBuilder
import com.cramsan.awslib.dsl.builders.EquippableItemBuilder
import com.cramsan.awslib.dsl.builders.KeyItemBuilder
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.EquippableType
import com.cramsan.awslib.utils.constants.InitialValues

class ConsumableItemBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var type = ConsumableType.INVALID
    var ammount = InitialValues.INVALID_AMOUNT

    internal fun build() = mapOf(
        id to ConsumableItemBuilder(
            id,
            type,
            ammount
        )
    )
}

class EquippableItemBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var range = InitialValues.INVALID_AMOUNT
    var accuracy: Double = InitialValues.INVALID_AMOUNT.toDouble()
    var damage: Double = InitialValues.INVALID_AMOUNT.toDouble()
    var type = EquippableType.INVALID

    internal fun build() = mapOf(
        id to EquippableItemBuilder(
            id,
            range,
            accuracy,
            damage,
            type
        )
    )
}

class KeyItemBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var name = InitialValues.INVALID_ID

    internal fun build() = mapOf(
        id to KeyItemBuilder(
            id,
        )
    )
}

class ItemInstanceBuilder(val itemBuilders: Map<String, EntityBuilder>) {
    var id = InitialValues.INVALID_ID
    var template = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY

    internal fun build(): ItemInterface {
        if (template == InitialValues.INVALID_ID) {
            throw RuntimeException("Template for entity with ID $id is not defined")
        }

        val builder = itemBuilders[template] ?: throw RuntimeException("Could not find template for $template")

        return when (builder) {
            is ConsumableItemBuilder -> builder.build(id, posX, posY)
            is EquippableItemBuilder -> builder.build(id, posX, posY)
            is KeyItemBuilder -> builder.build(id, posX, posY)
            else -> TODO()
        }
    }
}

class ItemBuildersBuilder {
    private val consumableItemBuilderList = mutableMapOf<String, EntityBuilder>()
    private val equippableItemBuilderList = mutableMapOf<String, EntityBuilder>()
    private val keyItemBuilderList = mutableMapOf<String, EntityBuilder>()

    fun consumable(block: ConsumableItemBuilderBuilder.() -> Unit) {
        consumableItemBuilderList.putAll(ConsumableItemBuilderBuilder().apply(block).build())
    }

    fun equippable(block: EquippableItemBuilderBuilder.() -> Unit) {
        equippableItemBuilderList.putAll(EquippableItemBuilderBuilder().apply(block).build())
    }

    fun keyItem(block: KeyItemBuilderBuilder.() -> Unit) {
        keyItemBuilderList.putAll(KeyItemBuilderBuilder().apply(block).build())
    }

    internal fun buildConsumableItems() = consumableItemBuilderList
    internal fun buildEquippableItems() = equippableItemBuilderList
    internal fun buildKeyItems() = keyItemBuilderList
}

class ItemListBuilder(
    val consumableBuilders: Map<String, EntityBuilder>,
    val equippableBuilders: Map<String, EntityBuilder>,
    val keyItemBuilders: Map<String, EntityBuilder>
) {
    private val itemList = mutableListOf<ItemInterface>()

    fun consumable(block: ItemInstanceBuilder.() -> Unit) {
        itemList.add(ItemInstanceBuilder(consumableBuilders).apply(block).build())
    }

    fun equipabble(block: ItemInstanceBuilder.() -> Unit) {
        itemList.add(ItemInstanceBuilder(equippableBuilders).apply(block).build())
    }

    fun keyItem(block: ItemInstanceBuilder.() -> Unit) {
        itemList.add(ItemInstanceBuilder(keyItemBuilders).apply(block).build())
    }

    internal fun build() = itemList
}
