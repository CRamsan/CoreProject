package com.cramsan.awslib

import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.map.MapLoader

import java.awt.*

class AWTRunner {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            EventQueue.invokeLater(::createAndShowGUI)
        }

        private fun createAndShowGUI() {

            val map = GameMap(MapLoader().loadCSVMap("map1.txt"))

            val sceneConfig = scene {
                player {
                    posX = 12
                    posY = 29
                    speed = 20
                }

                entities {
                    dog {
                        id = 5
                        posX = 15
                        posY = 26
                        priority = 5
                        enabled = false
                    }
                    scientist {
                        id = 1
                        group = 0
                        posX = 2
                        posY = 23
                    }
                }

                triggers {
                    entity {
                        id = 523
                        eventId = 912
                        targetId = 1
                        enabled = true

                    }
                }
                events {
                    interactive {
                        id = 912
                        text = "Welcome to this new game"
                    }
                    swapEntity {
                        id = 482
                        disableEntityId = 1
                    }
                }

            } ?: return
            val renderer = AWTRenderer()
            val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, renderer)
            renderer.startScene(entityManager, sceneConfig, map)
        }
    }
}
