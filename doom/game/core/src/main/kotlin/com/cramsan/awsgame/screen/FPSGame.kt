package com.cramsan.awsgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        }
        val entityManager = EntityManager(this.map.map, sceneConfig!!.triggerList, sceneConfig.eventList, this)
        scene = Scene(entityManager, sceneConfig)

        this.player = Player(sceneConfig.player)
        scene!!.loadScene()

        stage = Stage(StretchViewport(orthoCamera!!.viewportWidth, orthoCamera!!.viewportHeight, orthoCamera))
        Gdx.input.inputProcessor = stage

        val mySkin = Skin(Gdx.files.internal("skin/star-soldier-ui.json"))
        val parentTable = VerticalGroup()
        val mainPane = Table()
        mainPane.setFillParent(true)
        mainPane.add(parentTable).width(UIToolKit.DIALOG_WIDTH.toFloat())
            .pad(UIToolKit.DIALOG_PAD.toFloat())

        val row1 = HorizontalGroup()
        val health = Label("100", mySkin)
        val armor = Label("80", mySkin)
        val ammo = Label("20", mySkin)
        val direction = Label("N", mySkin)
        row1.addActor(health)
        row1.addActor(armor)
        row1.addActor(ammo)
        row1.addActor(direction)
        parentTable.addActor(row1)

        val row2 = HorizontalGroup()
        val up = TextButton("UP", mySkin)
        val down = TextButton("DOWN", mySkin)
        val left = TextButton("LEFT", mySkin)
        val right = TextButton("RIGHT", mySkin)
        row2.addActor(up)
        row2.addActor(down)
        row2.addActor(left)
        row2.addActor(right)
        parentTable.addActor(row2)

        stage!!.addActor(mainPane)
        stage!!.isDebugAll = true
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameViewport!!.update(width, height / 2)
        (gameViewport as StretchViewport).setScreenPosition(0, Gdx.graphics.height / 2)
        stage!!.viewport.update(width, height / 2, true);
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
        if(player!!.isAttacking) {
            GlobalScope.launch {
                scene!!.runTurn(TurnAction(TurnActionType.ATTACK, Direction.KEEP))
            }
        }
        camera!!.render(player!!, map)

        //render the UI
        stage!!.viewport.apply()
        stage?.act();
        stage!!.draw()
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

    override fun levelId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
