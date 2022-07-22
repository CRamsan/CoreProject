package com.cramsan.awslib.entitymanager.implementation

import com.cramsan.awslib.ai.AIRepo
import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.KeyItem
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.EntityManagerInterface
import com.cramsan.awslib.entitymanager.implementation.statemachine.CloseDoor
import com.cramsan.awslib.entitymanager.implementation.statemachine.ConsumeItem
import com.cramsan.awslib.entitymanager.implementation.statemachine.DamageEntity
import com.cramsan.awslib.entitymanager.implementation.statemachine.Disable
import com.cramsan.awslib.entitymanager.implementation.statemachine.DisplayInteractiveEvent
import com.cramsan.awslib.entitymanager.implementation.statemachine.DropItem
import com.cramsan.awslib.entitymanager.implementation.statemachine.Enable
import com.cramsan.awslib.entitymanager.implementation.statemachine.EndTurn
import com.cramsan.awslib.entitymanager.implementation.statemachine.EquipItem
import com.cramsan.awslib.entitymanager.implementation.statemachine.HideInteractiveEvent
import com.cramsan.awslib.entitymanager.implementation.statemachine.Move
import com.cramsan.awslib.entitymanager.implementation.statemachine.OpenDoor
import com.cramsan.awslib.entitymanager.implementation.statemachine.PickUpItem
import com.cramsan.awslib.entitymanager.implementation.statemachine.ReconstructItem
import com.cramsan.awslib.entitymanager.implementation.statemachine.StartTurn
import com.cramsan.awslib.entitymanager.implementation.statemachine.SwapCharacter
import com.cramsan.awslib.entitymanager.implementation.statemachine.Transition
import com.cramsan.awslib.entitymanager.implementation.statemachine.TransitionStack
import com.cramsan.awslib.entitymanager.implementation.statemachine.UnequiptItem
import com.cramsan.awslib.enums.DebugAction
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.NoopEvent
import com.cramsan.awslib.eventsystem.events.SwapCharacterEvent
import com.cramsan.awslib.eventsystem.triggers.CellTrigger
import com.cramsan.awslib.eventsystem.triggers.CharacterTrigger
import com.cramsan.awslib.eventsystem.triggers.Trigger
import com.cramsan.awslib.map.DoorCell
import com.cramsan.awslib.map.EndCell
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.map.OpenCell
import com.cramsan.awslib.map.WallCell
import com.cramsan.awslib.scene.SceneEventsCallback
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.constants.InitialValues.PLAYER_ID
import com.cramsan.framework.logging.EventLoggerInterface

class EntityManager(
    private val map: GameMap,
    private val triggerList: List<Trigger>,
    private val eventList: List<BaseEvent>,
    private val itemList: List<ItemInterface>,
    private var eventListener: EntityManagerEventListener?,
    private val log: EventLoggerInterface,
    private val aiRepo: AIRepo,
) : EntityManagerInterface, EntityManagerInteractionReceiver, TransitionStack.TransitionHandler {

    private val transitionStack = TransitionStack(this)

    val entityTriggerMap: HashMap<String, CharacterTrigger> = HashMap()

    var itemMap: Array<Array<ItemInterface?>> = Array(map.width) { arrayOfNulls<ItemInterface?>(map.height) }
    val itemSet = mutableSetOf<ItemInterface>()

    var triggerMap: Array<Array<CellTrigger?>> = Array(map.width) { arrayOfNulls<CellTrigger?>(map.height) }
    val triggerIdMap: HashMap<String, Trigger> = HashMap()

    val eventMap: HashMap<String, BaseEvent> = HashMap()
    var tmpInteractiveEvent: InteractiveEvent? = null

    private val tag = "EntityManager"

    var callback: SceneEventsCallback? = null

    init {
        triggerList.forEach {
            log.d(tag, "Trigger: $it")

            triggerIdMap.put(it.id, it)
            if (it is CharacterTrigger) {
                entityTriggerMap.put(it.targetId, it)
            } else if (it is CellTrigger) {
                triggerMap[it.posX][it.posY] = it
            }
        }
        eventList.forEach {
            log.d(tag, "Event: $it")
            eventMap.put(it.id, it)
        }
        itemList.forEach {
            log.d(tag, "Item: $it")
            registerItem(it)
        }
    }

    val characterSet = mutableSetOf<CharacterInterface>()
    val globalEntitySet = mutableSetOf<CharacterInterface>()

    val queue = mutableListOf<CharacterInterface>()

    var entityMap: Array<Array<CharacterInterface?>> = Array(map.width) {
        arrayOfNulls<CharacterInterface?>(map.height)
    }
    var entityIdMap: MutableMap<String, CharacterInterface> = mutableMapOf()

    internal fun register(entity: CharacterInterface) {
        globalEntitySet.add(entity)
        queue.add(entity)
        if (entity.enabled) {
            enableEntity(entity)
        }
    }

    internal fun enableEntity(entity: CharacterInterface) {
        entity.enabled = true
        characterSet.add(entity)
        entityIdMap[entity.id] = entity
        setPosition(entity, entity.posX, entity.posY)
    }

    internal fun disableEntity(entity: CharacterInterface) {
        entity.enabled = false
        characterSet.remove(entity)
        entityIdMap.remove(entity.id)
        entityMap[entity.posX][entity.posY] = null
    }

    internal fun registerItem(item: ItemInterface) {
        val overlappingItem = getItem(item.posX, item.posY)

        if (overlappingItem != null) {
            throw RuntimeException("Entity already registered at location: $item")
        }

        setItem(item)
    }

    override fun debugAction(debugAction: DebugAction) {
        when (debugAction) {
            DebugAction.STEP_BACK -> transitionStack.stepBack()
            DebugAction.STEP_FORWARD -> transitionStack.stepForward()
            DebugAction.REWIND_TURN -> transitionStack.rewindTurn()
            DebugAction.FF_TURN -> transitionStack.fastForwardTurn()
        }
    }

    override fun runTurns() {
        transitionStack.executeNewTransition(StartTurn(PLAYER_ID))
        queue.forEach {
            if (!it.enabled) {
                return@forEach
            }
            log.i(tag, "")
            log.i(tag, "Entity: $it")
            if (it.group != InitialValues.GROUP_PLAYER) {
                if (it.shouldMove) {
                    it.nextTurnAction = aiRepo.getNextTurnAction(it, this@EntityManager, map)
                } else {
                    it.nextTurnAction = TurnAction.NOOP
                }
            }
            if (it.nextTurnAction.turnActionType == TurnActionType.MOVE) {
                move(it)
            } else if (it.nextTurnAction.turnActionType == TurnActionType.ATTACK) {
                act(it)
            } else if (it.nextTurnAction.turnActionType == TurnActionType.NONE) {
                log.i(tag, "Noop action")
            } else {
                TODO("Unexpected TUrnActionType")
            }
            log.i(tag, "")
        }
    }

    private fun move(entity: CharacterInterface): Boolean {
        log.i(tag, "Moving")
        var x = entity.posX
        var y = entity.posY

        entity.nextTurnAction.direction.apply {
            when (this) {
                Direction.NORTH -> {
                    y--
                }
                Direction.SOUTH -> {
                    y++
                }
                Direction.WEST -> {
                    x--
                }
                Direction.EAST -> {
                    x++
                }
                Direction.KEEP -> {
                }
            }
        }
        log.d(tag, "Trying to move from x:${entity.posX},y:${entity.posY} -> x:$x,y:$y")

        if (map.isBlocked(x, y)) {
            log.d(tag, "Map location is blocked with ${map.cellAt(x, y)}")
            return false
        }

        if (isBlocked(x, y)) {
            log.d(tag, "Map location is blocked with ${getEntity(x, y)}")
            return false
        }

        transitionStack.executeNewTransition(
            Move(
                entity.id,
                x - entity.posX,
                y - entity.posY,
            ),
        )

        if (entity is Player) {
            pickUpItem(entity, x, y)
            doTileAction(x, y)
        }
        entity.nextTurnAction = TurnAction.NOOP

        return true
    }

    private fun actOnCell(entity: CharacterInterface, cellPosX: Int, cellPosY: Int) {
        val cell = map.cellAt(cellPosX, cellPosY)
        log.d(tag, "Target cell: $cell")

        when (cell) {
            is DoorCell -> {
                val transition = if (cell.opened) {
                    CloseDoor(entity.id, cellPosX, cellPosY)
                } else {
                    OpenDoor(entity.id, cellPosX, cellPosY)
                }
                transitionStack.executeNewTransition(transition)
            }
            is EndCell -> Unit
            is OpenCell -> Unit
            is WallCell -> Unit
        }
    }

    private fun act(entity: CharacterInterface): Boolean {
        log.i(tag, "Performing action")
        var x = entity.posX
        var y = entity.posY

        val direction = if (entity.nextTurnAction.direction == Direction.KEEP) {
            entity.heading
        } else {
            entity.nextTurnAction.direction
        }

        when (direction) {
            Direction.NORTH -> {
                y--
            }
            Direction.SOUTH -> {
                y++
            }
            Direction.WEST -> {
                x--
            }
            Direction.EAST -> {
                x++
            }
            else -> {
                TODO()
            }
        }
        log.d(tag, "Trying to target from x:${entity.posX},y:${entity.posY} -> x:$x,y:$y")

        val targetEntity = getEntity(x, y)
        if (targetEntity != null) {
            log.d(tag, "Target entity: $targetEntity")
            if (targetEntity.group != entity.group) {
                doDamage(entity, targetEntity)
            } else {
                doAction(targetEntity)
            }
        } else {
            actOnCell(entity, x, y)
        }

        entity.nextTurnAction = TurnAction.NOOP
        entity.heading = direction
        return true
    }

    internal fun setPosition(entity: CharacterInterface, poxX: Int, posY: Int): Boolean {
        if (isBlocked(poxX, posY)) {
            return false
        }
        entityMap[entity.posX][entity.posY] = null
        entityMap[poxX][posY] = entity
        entity.posX = poxX
        entity.posY = posY
        return true
    }

    internal fun doDamage(attacker: CharacterInterface, defender: CharacterInterface) {
        log.i(tag, "Performing damage")
        transitionStack.executeNewTransition(DamageEntity(attacker.id, defender, attacker.attack))
        if (defender.health <= 0) {
            log.i(tag, "Entity is dead: $defender")
            transitionStack.executeNewTransition(Disable(defender.id, defender))
            callback?.onCharacterChanged(defender)
        }
    }

    private fun doAction(receiver: CharacterInterface) {
        val trigger = entityTriggerMap.getValue(receiver.id)
        log.i(tag, "Performing action to trigger: $trigger")
        if (!trigger.enabled) {
            log.i(tag, "Trigger disabled")
            return
        }

        executeTrigger(trigger)
    }

    private fun pickUpItem(player: Player, posX: Int, posY: Int) {
        val item = getItem(posX, posY) ?: return

        // TODO: Perform logic when getting item
        val transition = when (item) {
            is ConsumableItem -> {
                when (item.type) {
                    ConsumableType.HEALTH -> player.health += item.ammount
                    ConsumableType.ARMOR -> TODO()
                    ConsumableType.CREDIT -> TODO()
                    ConsumableType.INVALID -> TODO()
                }
                ConsumeItem(player.id, item)
            }
            is KeyItem -> {
                PickUpItem(player.id, item)
            }
            is EquippableItem -> {
                EquipItem(player.id, item)
            }
            else -> {
                TODO("Invalid item")
            }
        }

        transitionStack.executeNewTransition(transition)
    }

    private fun doTileAction(posX: Int, posY: Int) {
        val trigger = triggerMap[posX][posY]
        if (trigger == null || !trigger.enabled) {
            return
        }

        log.i(tag, "Executing trigger: $trigger")
        executeTrigger(trigger)
    }

    internal fun handleSwapEntityEvent(event: SwapCharacterEvent): BaseEvent {
        val disableId = event.disableId
        val enableId = event.enableId

        val toDisableEntity = entityIdMap[disableId]
        var toEnableEntity: CharacterInterface? = null
        toDisableEntity?.let {
            disableEntity(it)
        }
        globalEntitySet.find { it.id == enableId }?.let {
            enableEntity(it)
            toEnableEntity = it
        }
        if (toEnableEntity != null && toDisableEntity != null) {
            toEnableEntity!!.posX = toDisableEntity.posX
            toEnableEntity!!.posY = toDisableEntity.posY
        }
        toDisableEntity?.let { callback?.onCharacterChanged(it) }
        toEnableEntity?.let { callback?.onCharacterChanged(it) }
        return if (event.nextEventId == InitialValues.NOOP_ID) {
            NoopEvent()
        } else {
            eventMap.getValue(event.nextEventId)
        }
    }

    private fun handleInteractiveEntityEvent(event: InteractiveEvent) {
        log.i(tag, "Handling Interactive Event: $event")
        log.d(tag, "Text: ${event.text}")
        tmpInteractiveEvent = event
        event.options.forEach {
            log.d(tag, "Option: $it")
        }

        // TODO: Shouldn't this be a non-nullable?
        log.i(tag, "Calling Callback: $eventListener")
        eventListener?.onInteractionRequired(event.text, event.options, this@EntityManager)
        log.d(tag, "Waiting for response from Event: $event")

        /*
        log.d(tag, "Got response from Event: $event")
        log.i(tag, "Received Event: $receivedEvent")
        return if (receivedEvent.type == EventType.NOOP) {
            NoopEvent()
        } else {
            receivedEvent
        }
         */
    }

    internal fun handleChangeTriggerEvent(event: ChangeTriggerEvent): BaseEvent {
        val disableId = event.disableId
        val enableId = event.enableId

        triggerIdMap[enableId]?.enabled = true
        triggerIdMap[disableId]?.enabled = false

        return if (event.nextEventId == InitialValues.NOOP_ID) {
            NoopEvent()
        } else {
            eventMap.getValue(event.nextEventId)
        }
    }

    private fun executeTrigger(trigger: Trigger) {
        val nextEvent = eventMap[trigger.eventId]
        nextEvent?.let {
            executeEvent(it)
        }
    }

    private fun executeEvent(event: BaseEvent) {
        var oldEvent: BaseEvent? = null
        var nextEvent: BaseEvent? = event
        while (nextEvent != null) {
            var localNextEvent = nextEvent
            log.i(tag, "Event: $localNextEvent")

            when (localNextEvent) {
                is SwapCharacterEvent -> {
                    log.d(tag, "Swap event")
                    transitionStack.executeNewTransition(
                        SwapCharacter(PLAYER_ID, oldEvent?.id ?: InitialValues.NOOP_ID, localNextEvent),
                    )
                    oldEvent = localNextEvent
                    nextEvent = if (localNextEvent.nextEventId == InitialValues.NOOP_ID) {
                        NoopEvent()
                    } else {
                        eventMap.getValue(localNextEvent.nextEventId)
                    }
                }
                is InteractiveEvent -> {
                    log.d(tag, "Interactive event")
                    transitionStack.executeNewTransition(DisplayInteractiveEvent(PLAYER_ID, localNextEvent))
                    nextEvent = null
                }
                is ChangeTriggerEvent -> {
                    log.d(tag, "Change trigger event")
                    // nextEvent = handleChangeTriggerEvent(localNextEvent)
                    TODO("Unexpected Event type")
                }
                is NoopEvent -> {
                    log.i(tag, "Noop event")
                    nextEvent = null
                }
                else -> {
                    TODO("Unexpected Event type")
                }
            }
        }
    }

    override fun selectOption(option: InteractiveEventOption?) {
        log.i(tag, "Selection: $option")
        transitionStack.executeNewTransition(HideInteractiveEvent(PLAYER_ID, tmpInteractiveEvent!!))
        tmpInteractiveEvent = null
        if (option == null) {
            return
        }

        if (option.eventId == InitialValues.NOOP_ID) {
            log.i(tag, "SelectIotuin INVALID")
            return
        }

        val eEvent = eventMap.getValue(option.eventId)
        log.i(tag, "Event: $eEvent")
        executeEvent(eEvent)
    }

    internal fun processGameEntityState() {
        transitionStack.executeNewTransition(EndTurn(PLAYER_ID))
    }

    internal fun getEntity(posX: Int, posY: Int): CharacterInterface? {
        return entityMap[posX][posY]
    }

    internal fun clearItem(item: ItemInterface) {
        if (getEntity(item.posX, item.posY) == null) {
            TODO("Trying to clear an already empty item")
        }
        itemSet.remove(item)
        itemMap[item.posX][item.posY] = null
    }

    internal fun setItem(item: ItemInterface) {
        if (getItem(item.posX, item.posY) != null) {
            TODO("Trying to overwrite an existing item")
        }
        itemSet.add(item)
        itemMap[item.posX][item.posY] = item
    }

    internal fun getItem(posX: Int, posY: Int): ItemInterface? {
        return itemMap[posX][posY]
    }

    internal fun isBlocked(posX: Int, posY: Int): Boolean {
        if (posX < 0 || posX > entityMap.lastIndex || posY < 0 || posY > entityMap.first().lastIndex)
            return true

        return entityMap[posX][posY] != null
    }

    private fun doMove(moveTransition: Move) {
        val entity = entityIdMap.getValue(moveTransition.id)
        val x = entity.posX + moveTransition.dx
        val y = entity.posY + moveTransition.dy
        setPosition(entity, x, y)

        callback?.onCharacterChanged(entity)

        // TODO: Game end should be done as a trigger
        if (map.cellAt(x, y).terrain == TerrainType.END) {
            callback?.onSceneEnded(true)
        }
    }

    private fun openDoorCell(cellPosX: Int, cellPosY: Int) {
        val cell = map.cellAt(cellPosX, cellPosY)
        log.d(tag, "Target cell: $cell")

        if (cell !is DoorCell) {
            throw RuntimeException("Unexpected cell type: $cell")
        }
        if (cell.opened) {
            throw RuntimeException("Cell door already open")
        }
        cell.opened = true
        callback?.onCellChanged(cell)
    }

    private fun closeDoorCell(cellPosX: Int, cellPosY: Int) {
        val cell = map.cellAt(cellPosX, cellPosY)
        log.d(tag, "Target cell: $cell")

        if (cell !is DoorCell) {
            throw RuntimeException("Unexpected cell type: $cell")
        }
        if (!cell.opened) {
            throw RuntimeException("Cell door already closed")
        }
        cell.opened = false
        callback?.onCellChanged(cell)
    }

    private fun consumeItem(entityId: String, consumableItem: ConsumableItem) {
        val player = entityIdMap.getValue(entityId)
        when (consumableItem.type) {
            ConsumableType.HEALTH -> player.health += consumableItem.ammount
            ConsumableType.ARMOR -> TODO()
            ConsumableType.CREDIT -> TODO()
            ConsumableType.INVALID -> TODO()
        }
        clearItem(consumableItem)
    }

    private fun reconstructItem(entityId: String, consumableItem: ConsumableItem) {
        val player = entityIdMap.getValue(entityId)
        when (consumableItem.type) {
            ConsumableType.HEALTH -> player.health -= consumableItem.ammount
            ConsumableType.ARMOR -> TODO()
            ConsumableType.CREDIT -> TODO()
            ConsumableType.INVALID -> TODO()
        }
        setItem(consumableItem)
    }

    private fun damageEntity(target: CharacterInterface, damage: Int) {
        target.health -= damage
    }

    @Suppress("UNUSED_PARAMETER")
    private fun pickUpItem(entityId: String, consumableItem: ConsumableItem) {
        /*
        val player = entityIdMap.getValue(entityId)
        is KeyItem -> {
            player.keyItemList.add(item)
        }
        is EquippableItem -> {
            player.equipableItemList.add(item)
        }
         */
    }

    override fun executeTransition(transition: Transition) {
        when (transition) {
            is Enable -> enableEntity(transition.target)
            is Disable -> disableEntity(transition.target)
            /*
            is CreateCharacter -> TODO()
            is CreateItem -> TODO()
            is DestroyCharacter -> TODO()
            is DestroyItem -> TODO()
             */
            is Move -> doMove(transition)
            is StartTurn -> Unit
            is EndTurn -> Unit
            is CloseDoor -> closeDoorCell(transition.posX, transition.posY)
            is OpenDoor -> openDoorCell(transition.posX, transition.posY)
            is ConsumeItem -> consumeItem(transition.id, transition.consumableItem)
            is ReconstructItem -> reconstructItem(transition.id, transition.consumableItem)
            is PickUpItem -> TODO()
            is DropItem -> TODO()
            is EquipItem -> TODO()
            is UnequiptItem -> TODO()
            is SwapCharacter -> handleSwapEntityEvent(transition.event)
            is DamageEntity -> damageEntity(transition.target, transition.damage)
            is DisplayInteractiveEvent -> handleInteractiveEntityEvent(transition.event)
            is HideInteractiveEvent -> Unit
        }
    }
}
