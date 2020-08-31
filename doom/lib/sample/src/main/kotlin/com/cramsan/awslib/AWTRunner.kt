package com.cramsan.awslib

import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.map.MapLoader
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerJVM
import kotlinx.coroutines.runBlocking
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.awt.EventQueue

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

                characters {
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
                    scientist {
                        id = 2
                        group = 0
                        posX = 4
                        posY = 23
                    }
                }

                triggers {
                    character {
                        id = 523
                        eventId = 912
                        targetId = 1
                        enabled = true
                    }
                    character {
                        id = 525
                        eventId = 482
                        targetId = 2
                        enabled = true
                    }
                }
                events {
                    interactive {
                        id = 912
                        text = "Welcome to this new game"
                    }
                    swapCharacter {
                        id = 482
                        enableCharacterId = 5
                        disableCharacterId = 2
                        nextEventId = InitialValues.NOOP_ID
                    }
                }
                items {
                    health {
                        posX = 4
                        posY = 20
                    }
                }
            } ?: return

            val kodein = DI {
                bind() from singleton { EventLogger(Severity.VERBOSE, null, LoggerJVM()) }
                bind() from singleton { HaltUtil(HaltUtilJVM()) }
                bind() from singleton { AssertUtil(true, instance(), instance()) }
            }

            val renderer = AWTRenderer(kodein)
            val entityManager = EntityManager(map, sceneConfig.triggerList, sceneConfig.eventList, sceneConfig.itemList, renderer, kodein)

            runBlocking {
                renderer.startScene(entityManager, sceneConfig, map)
            }
        }
    }
}
