package com.cramsan.ps2link.service.di

import com.cramsan.ps2link.service.controller.domain.CharacterController
import com.cramsan.ps2link.service.controller.domain.ItemController
import com.cramsan.ps2link.service.controller.domain.WSController
import com.cramsan.ps2link.service.repository.mongo.CharacterRepository
import com.cramsan.ps2link.service.repository.mongo.ItemRepository
import com.cramsan.ps2link.service.service.PlayerTracker
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Initialized instances to be used at the application layer. The classes here pertain to the bejaviour of this
 * application.
 */
val ApplicationModule = module {

    single<CharacterRepository> {
        CharacterRepository(
            get(named(COLLECTION_CHARACTER_NAME)),
            get(),
        )
    }

    single<ItemRepository> {
        ItemRepository(
            get(named(COLLECTION_ITEM_NAME)),
            get(),
        )
    }

    single {
        CharacterController(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single {
        ItemController(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single {
        PlayerTracker(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single {
        WSController(
            get(),
            get(),
            get(),
            get(),
        )
    }
}
