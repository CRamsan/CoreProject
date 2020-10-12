package com.cramsan.awslib

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.Enemy
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.KeyItem
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.implementation.EntityManager
import com.cramsan.awslib.entitymanager.implementation.TurnAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.map.Cell
import com.cramsan.awslib.map.DoorCell
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.scene.Scene
import com.cramsan.awslib.scene.SceneConfig
import com.cramsan.awslib.scene.SceneEventsCallback
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.logging.EventLoggerInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JOptionPane.showInputDialog
import javax.swing.JPanel
import kotlin.system.exitProcess

class AWTRenderer(
    val eventLoggerInterface: EventLoggerInterface,
    val haltUtilInterface: HaltUtilInterface,
    val assertUtilInterface: AssertUtilInterface
) : JFrame(), EntityManagerEventListener {

    var isGameRunning = true

    init {
        setSize(400, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        isAlwaysOnTop = true
    }

    suspend fun startScene(manager: EntityManager, sceneConfig: SceneConfig, map: GameMap) {
        add(RendererCanvas(manager, map))
        val mainPlayer = sceneConfig.player
        val gameLoop = GlobalScope.async {
            val scene = Scene(manager, sceneConfig, eventLoggerInterface)
            scene.setListener(
                object : SceneEventsCallback {
                    override fun onEntityChanged(entity: GameEntityInterface) {
                        when (entity) {
                            is CharacterInterface -> {
                                if (entity == mainPlayer) {
                                    if (!entity.enabled) {
                                        println("Player is dead")
                                        isGameRunning = false
                                    }
                                }
                            }
                        }
                        repaint()
                    }

                    override fun onCellChanged(cell: Cell) {
                        repaint()
                    }

                    override fun onSceneEnded(completed: Boolean) {
                        exitProcess(0)
                    }
                }
            )

            this@AWTRenderer.addKeyListener(
                object : KeyAdapter() {
                    override fun keyReleased(e: KeyEvent?) {
                        if (!isGameRunning) {
                            return
                        }

                        when (e?.keyCode) {
                            KeyEvent.VK_UP -> {
                                mainPlayer.heading = Direction.NORTH
                                return
                            }
                            KeyEvent.VK_DOWN -> {
                                mainPlayer.heading = Direction.SOUTH
                                return
                            }
                            KeyEvent.VK_LEFT -> {
                                mainPlayer.heading = Direction.WEST
                                return
                            }
                            KeyEvent.VK_RIGHT -> {
                                mainPlayer.heading = Direction.EAST
                                return
                            }
                            else -> return
                        }
                    }
                    override fun keyTyped(e: KeyEvent?) {
                        if (!isGameRunning) {
                            return
                        }

                        val action = when (e?.keyChar) {
                            'w' -> {
                                mainPlayer.heading = Direction.NORTH
                                TurnAction(TurnActionType.MOVE, Direction.NORTH)
                            }
                            's' -> {
                                mainPlayer.heading = Direction.SOUTH
                                TurnAction(TurnActionType.MOVE, Direction.SOUTH)
                            }
                            'a' -> {
                                mainPlayer.heading = Direction.WEST
                                TurnAction(TurnActionType.MOVE, Direction.WEST)
                            }
                            'd' -> {
                                mainPlayer.heading = Direction.EAST
                                TurnAction(TurnActionType.MOVE, Direction.EAST)
                            }
                            ' ' -> TurnAction(TurnActionType.ATTACK, Direction.KEEP)
                            else -> TurnAction(TurnActionType.NONE, Direction.KEEP)
                        }
                        GlobalScope.launch {
                            scene.runTurn(action)
                            repaint()
                        }
                    }
                }
            )
            scene.loadScene()
        }
        gameLoop.await()
    }

    override fun onGameReady(eventReceiver: EntityManagerInteractionReceiver) {
    }
    override fun onTurnCompleted(eventReceiver: EntityManagerInteractionReceiver) {
    }
    override fun onInteractionRequired(text: String, options: List<InteractiveEventOption>, eventReceiver: EntityManagerInteractionReceiver) {
        System.out.println("Options: ")
        options.forEachIndexed { index, interactiveEventOption ->
            System.out.println("$index) $interactiveEventOption")
        }

        val possibilities = Array(options.size) { i -> options[i].label }

        if (possibilities.size > 0) {
            val resultOption = showInputDialog(
                this@AWTRenderer,
                text,
                "Sample interactive event",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities.first()
            ) as String

            var selection = -1
            var selectedOption: InteractiveEventOption?

            options.forEachIndexed { index, interactiveEventOption ->
                if (interactiveEventOption.label == resultOption) {
                    selection = index
                }
            }

            selectedOption = options[selection]

            System.out.println("You selected $selectedOption")
            GlobalScope.launch {
                eventReceiver.selectOption(selectedOption)
            }
        } else {
            showInputDialog(text)
            System.out.println("Continuing")
            GlobalScope.launch {
                eventReceiver.selectOption(null)
            }
        }
    }

    internal inner class RendererCanvas(
        val manager: EntityManager,
        val map: GameMap
    ) : JPanel() {

        override fun paint(graphics: Graphics?) {
            super.paint(graphics)
            val g = graphics as Graphics2D
            val width = map.width
            val height = map.height
            val windowsHeight = getHeight()
            val windowsWidth = getWidth()

            val cellWidth = Math.round(windowsWidth.toFloat() / width)
            val cellHeight = Math.round(windowsHeight.toFloat() / height)

            for (y in 0 until height) {
                for (x in 0 until width) {
                    val cell = map.cellAt(x, y)

                    when (cell.terrain) {
                        TerrainType.OPEN -> g.color = Color.WHITE
                        TerrainType.WALL -> g.color = Color.BLACK
                        TerrainType.DOOR -> {
                            val doorCell = cell as DoorCell
                            if (doorCell.opened)
                                g.color = Color.CYAN
                            else
                                g.color = Color.LIGHT_GRAY
                        }
                        TerrainType.END -> g.color = Color.GREEN
                    }
                    g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight)
                }
            }

            for (entity in manager.characterSet) {
                when (entity) {
                    is Player -> g.color = Color.BLUE
                    is Enemy -> g.color = Color.RED
                    is Ally -> g.color = Color.GREEN
                }
                g.fillRect(
                    entity.posX * cellWidth,
                    entity.posY * cellHeight,
                    cellWidth,
                    cellHeight
                )
            }

            manager.itemSet.forEach {
                when (it) {
                    is ConsumableItem -> {
                        g.color = Color.YELLOW
                    }
                    is EquippableItem -> {
                        g.color = Color.ORANGE
                    }
                    is KeyItem -> {
                        g.color = Color.PINK
                    }
                    else -> {
                        TODO("Invalid item")
                    }
                }
                g.fillOval(
                    it.posX * cellWidth,
                    it.posY * cellHeight,
                    cellWidth / 2,
                    cellHeight / 2
                )
            }
        }
    }
}
