package com.cramsan.awslib.dsl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DSLParserTests {

    @Test
    fun basicSceneTest() {
        val scene = scene {
            player {
                posX = 1
                posY = 1
                speed = 20
            }

            characters {
                dog {
                    id = 1
                    posX = 10
                    posY = 20
                    priority = 5
                }
            }

            triggers {
                cell {
                }
                character {
                }
            }

            events {
                interactive {
                }
                swapCharacter {
                }
                changeTrigger {
                }
            }
        }
        assertNotNull(scene)
        assertEquals(20, scene.characterList.first().posY)
        assertEquals(2, scene.triggerList.size)
    }
}
