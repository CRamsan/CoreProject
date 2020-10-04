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

            entityBuilders {
                enemy {
                    id = "1"
                }
            }

            entity {
                enemy {
                    id = "1"
                    template = "1"
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

    @Test
    fun anotherBasicSceneTest() {
        val scene = scene {
            player {
                posX = 1
                posY = 1
                speed = 20
            }
            itemBuilders {
                consumable {
                    id = "small-medkit"
                    type = ConsumableType.HEALTH
                    ammount = 10
                }
                consumable {
                    id = "large-medkit"
                    type = ConsumableType.HEALTH
                    ammount = 25
                }
                equippable {
                    id = "axe"
                    range = 1
                    accuracy = 1.0
                    damage = 1.0
                }
                equippable {
                    id = "handgun"
                    range = 4
                    accuracy = 0.75
                    damage = 0.75
                }
                keyItem {
                    id = "red-key"
                    name = "Red Key"
                }
                keyItem {
                    id = "blue-key"
                    name = "Blue Key"
                }
            }
            entityBuilders {
                ally {
                    id = "civ-1"
                    name = "Civilian 1"
                    type = AllyType.CIVILIAN
                }
                ally {
                    id = "scientist-1"
                    name = "Scientist 1"
                    type = AllyType.SCIENTIST
                }
                enemy {
                    id = "dog-1"
                    name = "Dog"
                    type = EnemyType.DOG
                    range = 1
                    damage = 1.0
                    accuracy = 1.0
                }
                enemy {
                    id = "soldier-1"
                    name = "Soldier"
                    type = EnemyType.SOLDIER
                    range = 3
                    damage = 1.0
                    accuracy = 0.75
                }
            }
        }
    }
}
