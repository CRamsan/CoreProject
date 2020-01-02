package com.cramsan.awsgame

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.scene.Scene
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * This code is based on the raycaster demo from https://github.com/walle/raycaster
 */
class AWSGame : ApplicationAdapter(), EntityManagerEventListener {

    private var player: Player? = null
    private var scene: Scene? = null
    private lateinit var map: Map
    private var controls: Controls? = null
    private var camera: Camera? = null

    private var seconds = 0f
    private var viewport: Rectangle? = null
    private var scale = 1f
    private var orthoCamera: com.badlogic.gdx.graphics.OrthographicCamera? = null

    override fun create() {
        // Setup 2d camera with top left coordinates
        // http://stackoverflow.com/questions/7708379/changing-the-coordinate-system-in-libgdx-java/7751183#7751183
        // This forces us to flip textures on the y axis, eg. in Camera#drawSky
        orthoCamera = com.badlogic.gdx.graphics.OrthographicCamera(VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())
        orthoCamera!!.setToOrtho(true, VIRTUAL_WIDTH.toFloat(), VIRTUAL_HEIGHT.toFloat())

        this.map = Map(32)
        this.controls = Controls()
        this.camera = Camera(orthoCamera!!, 320.0, Math.PI * 0.4)

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
        val entityManager = EntityManager(this.map.map, sceneConfig.triggerList, sceneConfig.eventList, this)
        scene = Scene(entityManager, sceneConfig)

        this.player = Player(sceneConfig.player)

        scene!!.loadScene()
    }

    override fun resize(width: Int, height: Int) {
        // calculate new viewport
        val aspectRatio = width.toFloat() / height.toFloat()

        val crop = com.badlogic.gdx.math.Vector2(0f, 0f)
        if (aspectRatio > ASPECT_RATIO) {
            scale = height.toFloat() / VIRTUAL_HEIGHT.toFloat()
            crop.x = (width - VIRTUAL_WIDTH * scale) / 2f
        } else if (aspectRatio < ASPECT_RATIO) {
            scale = width.toFloat() / VIRTUAL_WIDTH.toFloat()
            crop.y = (height - VIRTUAL_HEIGHT * scale) / 2f
        } else {
            scale = width.toFloat() / VIRTUAL_WIDTH.toFloat()
        }

        val w = VIRTUAL_WIDTH.toFloat() * scale
        val h = VIRTUAL_HEIGHT.toFloat() * scale
        viewport = Rectangle(crop.x, crop.y, w, h)
    }

    override fun render() {
        if (input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        orthoCamera!!.update()
        Gdx.gl.glViewport(viewport!!.x.toInt(), viewport!!.y.toInt(), viewport!!.width.toInt(), viewport!!.height.toInt())

        seconds = Gdx.graphics.deltaTime

        map.update()
        controls!!.update()
        val input = controls!!.input
        player!!.update(seconds, input)
        player!!.move?.let {
            GlobalScope.launch {
                scene!!.runTurn(TurnAction(TurnActionType.MOVE, it))
            }
        }
        if(player!!.isAttacking) {
            GlobalScope.launch {
                scene!!.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
            }
        }
        camera!!.render(player!!, map)
    }

    override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInteractionRequired(
        text: String?,
        options: List<InteractiveEventOption>,
        eventReceiver: EntityManagerInteractionReceiver
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val CIRCLE = Math.PI * 2

        private val VIRTUAL_WIDTH = 1024
        private val VIRTUAL_HEIGHT = 640
        private val ASPECT_RATIO = VIRTUAL_WIDTH.toFloat() / VIRTUAL_HEIGHT.toFloat()
    }
}
