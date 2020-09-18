package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.cramsan.awsgame.Controls
import com.cramsan.awsgame.renderer.Camera
import com.cramsan.awsgame.renderer.Map
import com.cramsan.awsgame.renderer.Player
import com.cramsan.awsgame.subsystems.ui.UIToolKit
import com.cramsan.awslib.dsl.scene
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.scene.Scene
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerJVM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.cramsan.awslib.ai.implementation.DummyAIRepoImpl

/**
 * This code is based on the raycaster demo from https://github.com/walle/raycaster
 */
class FPSGame : GameScreen(), EntityManagerEventListener {

    private var stage: Stage? = null

    private var player: Player? = null
    private var scene: Scene? = null
    private lateinit var map: Map
    private var controls: Controls? = null
    private var camera: Camera? = null

    private var seconds = 0f

    private var gameViewport: Viewport? = null
    private var orthoCamera: OrthographicCamera? = null
    private var uiCamera: OrthographicCamera? = null
    private var mySkin: Skin? = null

    override fun screenInit() {
        super.screenInit()

        // Setup 2d camera with top left coordinates
        // http://stackoverflow.com/questions/7708379/changing-the-coordinate-system-in-libgdx-java/7751183#7751183
        // This forces us to flip textures on the y axis, eg. in Camera#drawSky
        orthoCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), ((Gdx.graphics.height / 2).toFloat()))
        orthoCamera!!.setToOrtho(true)
        gameViewport = StretchViewport(orthoCamera!!.viewportWidth, orthoCamera!!.viewportHeight, orthoCamera)
        (gameViewport as StretchViewport).setScreenPosition(0, Gdx.graphics.height / 2)

        this.map = Map(32)
        this.controls = Controls()
        this.camera = Camera(orthoCamera!!, orthoCamera!!.viewportWidth.toDouble(), Math.PI * 0.4, gameViewport as StretchViewport)

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
            }

            triggers {
                character {
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
                swapCharacter {
                    id = 482
                    disableCharacterId = 1
                }
            }
        }

        val logger = EventLogger(Severity.INFO, null, LoggerJVM())
        // val haltUtil = HaltUtil(HaltUtilJVM())
        // val assert = AssertUtil(true, logger, haltUtil)
        val aiRepo = DummyAIRepoImpl(logger)

        val entityManager = EntityManager(this.map.map, sceneConfig!!.triggerList, sceneConfig.eventList, sceneConfig.itemList, this, logger, aiRepo)
        scene = Scene(entityManager, sceneConfig, logger)

        this.player = Player(sceneConfig.player)
        scene!!.loadScene()
        uiCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), ((Gdx.graphics.height / 2).toFloat()))
        uiCamera!!.setToOrtho(false)
        stage = Stage(StretchViewport(uiCamera!!.viewportWidth, uiCamera!!.viewportHeight, uiCamera))
        Gdx.input.inputProcessor = stage

        mySkin = Skin(Gdx.files.internal("skin/star-soldier-ui.json"))

        val mainPane = UIToolKit.createNavigationMenu(mySkin!!)

        stage!!.addActor(mainPane)
        stage!!.isDebugAll = true
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameViewport!!.update(width, height / 2)
        (gameViewport as StretchViewport).setScreenPosition(0, Gdx.graphics.height / 2)
        stage!!.viewport.update(width, height / 2, true)
    }

    override fun performRender() {
        super.performRender()
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        orthoCamera!!.update()
        gameViewport!!.apply()
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
        if (player!!.isAttacking) {
            GlobalScope.launch {
                scene!!.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
            }
        }
        camera!!.render(player!!, map)

        // render the UI
        uiCamera!!.update()
        stage!!.viewport.apply()
        stage?.act()
        stage!!.draw()
    }

    override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onInteractionRequired(
        text: String,
        options: List<InteractiveEventOption>,
        eventReceiver: EntityManagerInteractionReceiver
    ) {
        val newPane = UIToolKit.createTextPane(this.mySkin!!, text, options, eventReceiver)
        stage!!.addActor(newPane)
    }

    override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun levelId(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
