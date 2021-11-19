package com.cramsan.stranded.gdx.ui.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.cramsan.stranded.gdx.ui.BaseUIComponent
import com.cramsan.stranded.lib.client.ui.game.widget.ShelterWidget
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton

class ShelterUIGdx : BaseUIComponent(), ShelterWidget {

    override val widget: Actor

    private var shelterList: List<Shelter> = emptyList()
    private val contentHolder: Table

    init {
        widget = scene2d {
            table {
                contentHolder = this
            }
        }
    }

    private fun recompose() {
        contentHolder.clearChildren()

        shelterList.forEach { shelter ->
            contentHolder.add(
                scene2d {
                    textButton("Holding: ${shelter.playerList.size}")
                }
            )
        }
        contentHolder.invalidate()
    }

    override fun setShelterList(shelterList: List<Shelter>) {
        this.shelterList = shelterList
        recompose()
    }
}
