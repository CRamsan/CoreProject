package com.cramsan.stranded.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.cramsan.stranded.lib.messages.module
import com.cramsan.stranded.lib.storage.FileBasedCardRepository
import kotlinx.serialization.json.Json
import ktx.app.KtxGame
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class StrandedGame : KtxGame<Screen>(null) {

    override fun create() {
        super.create()
        KtxAsync.initiate()

        val cardRepository = FileBasedCardRepository(
            filename = "test.json",
            json = Json {
                serializersModule = module
                prettyPrint = true
            }
        )
        cardRepository.initialize()

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("uiskin.json"))
        addScreen(
            MainMenuScreen::class.java,
            MainMenuScreen(cardRepository)
        )
        setScreen<MainMenuScreen>()
    }
}
