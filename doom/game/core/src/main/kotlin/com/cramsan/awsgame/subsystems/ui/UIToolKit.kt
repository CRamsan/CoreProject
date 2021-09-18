package com.cramsan.awsgame.subsystems.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * This class provides helped methods to create UI elements. Here is where the UI layout is defined.
 */
object UIToolKit {

    fun createNavigationMenu(skin: Skin): Actor {
        val mainPane = Table(skin)
        mainPane.setFillParent(true)

        val health = Label("100", skin)
        val armor = Label("80", skin)
        val ammo = Label("20", skin)
        val direction = Label("N", skin)
        mainPane.add(health)
        mainPane.add(armor)
        mainPane.add(ammo)
        mainPane.add(direction)
        mainPane.row()

        val up = TextButton("UP", skin)
        val down = TextButton("DOWN", skin)
        val left = TextButton("LEFT", skin)
        val right = TextButton("RIGHT", skin)
        mainPane.add(up).width(Value.percentWidth(0.25F, mainPane)).center()
        mainPane.add(down).width(Value.percentWidth(0.25F, mainPane)).center()
        mainPane.add(left).width(Value.percentWidth(0.25F, mainPane)).center()
        mainPane.add(right).width(Value.percentWidth(0.25F, mainPane)).center()
        mainPane.row()
        return mainPane
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createTextPane(
        skin: Skin,
        text: String,
        options: List<InteractiveEventOption>,
        eventReceiver: EntityManagerInteractionReceiver
    ): Actor {
        val mainPane = Table(skin)
        mainPane.setFillParent(true)

        val textPane = Label(text, skin)
        textPane.setWrap(true)
        mainPane.add(textPane).fillX()
        mainPane.row()

        if (options.isNotEmpty()) {
            var selectedOption: InteractiveEventOption?
            options.forEachIndexed { index, _ ->
                val button = TextButton(text, skin)
                button.addListener(
                    EventListener {
                        selectedOption = options[index]
                        GlobalScope.launch {
                            eventReceiver.selectOption(selectedOption)
                        }
                        mainPane.removeActor(mainPane)
                        true
                    }
                )
                mainPane.add(button).pad(5F)
            }
        } else {
            val continueButton = TextButton("Continue", skin)
            continueButton.addListener(
                EventListener {
                    GlobalScope.launch {
                        eventReceiver.selectOption(null)
                    }
                    mainPane.removeActor(mainPane)
                    true
                }
            )
            mainPane.add(continueButton)
        }

        return mainPane
    }
}
