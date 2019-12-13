package com.cramsan.awslib.entitymanager.implementation

import com.cramsan.awslib.ai.`interface`.AIRepo
import com.cramsan.awslib.scene.SceneEventsCallback
import com.cramsan.awslib.ai.implementation.DummyAIRepoImpl
import com.cramsan.awslib.entity.GameEntityInterface
import com.cramsan.awslib.entitymanager.EntityManagerEventListener
import com.cramsan.awslib.entitymanager.EntityManagerInteractionReceiver
import com.cramsan.awslib.entitymanager.EntityManagerInterface
import com.cramsan.awslib.enums.Direction
import com.cramsan.awslib.enums.TurnActionType
import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.enums.TerrainType
import com.cramsan.awslib.eventsystem.triggers.CellTrigger
import com.cramsan.awslib.eventsystem.GameEntityTrigger
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.EventType
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.NoopEvent
import com.cramsan.awslib.eventsystem.events.SwapEntityEvent
import com.cramsan.awslib.eventsystem.triggers.Trigger
import com.cramsan.awslib.map.GameMap
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.logging.Logger
import com.cramsan.awslib.utils.logging.Severity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class EntityManager(
    private val map: GameMap,
    private val triggerList: List<Trigger>,
    private val eventList: List<BaseEvent>,
    private var eventListener: EntityManagerEventListener?,
    private val aiRepo: AIRepo = DummyAIRepoImpl()
) : EntityManagerInterface, EntityManagerInteractionReceiver {

    val entityTriggerMap: HashMap<Int, GameEntityTrigger> = HashMap()

    var triggerMap: Array<Array<CellTrigger?>> = Array(map.width) { arrayOfNulls<CellTrigger?>(map.height) }
    val triggerIdMap: HashMap<Int, Trigger> = HashMap()

    val eventMap: HashMap<Int, BaseEvent> = HashMap()
    var nextEvent: BaseEvent? = null
    val channel = Channel<BaseEvent>()

    init {
        triggerList.forEach {
            triggerIdMap.put(it.id, it)
            if (it is GameEntityTrigger) {
                entityTriggerMap.put(it.targetId, it)
            } else if (it is CellTrigger) {
                triggerMap[it.posX][it.posY] = it
            }
        }
        eventList.forEach {
            eventMap.put(it.id, it)
        }
    }

    val entitySet = mutableSetOf<GameEntityInterface>()
    val disabledEntitySet = mutableSetOf<GameEntityInterface>()

    val queue = mutableListOf<GameEntityInterface>()

    val removeSet = mutableSetOf<GameEntityInterface>()
    val addSet = mutableSetOf<GameEntityInterface>()

    var entityMap: Array<Array<GameEntityInterface?>> = Array(map.width) { arrayOfNulls<GameEntityInterface?>(map.height) }
    var entityIdMap: MutableMap<Int, GameEntityInterface> = mutableMapOf()

    override fun register(entity: GameEntityInterface): Boolean {
        if (!entity.enabled) {
            disabledEntitySet.add(entity)
            return true
        }

        if (isBlocked(entity.posX, entity.posY)) {
            return false
        }

        if (entitySet.contains(entity)) {
            return false
        }

        if (entityIdMap.containsKey(entity.id)) {
            return false
        }

        entitySet.add(entity)
        entityIdMap[entity.id] = entity
        queue.add(entity)
        setPosition(entity, entity.posX, entity.posY)
        return true
    }

    override fun deregister(entity: GameEntityInterface): Boolean {
        if (!entitySet.contains(entity)) {
            return false
        }
        entityIdMap.remove(entity.id)
        queue.remove(entity)
        entityMap[entity.posX][entity.posY] = null
        entitySet.remove(entity)
        disabledEntitySet.add(entity)
        entity.enabled = false
        return true
    }

    override fun setEntityState(entity: GameEntityInterface, enabled: Boolean) {
        if (enabled) {
            prepareForAdd(entity)
        } else {
            prepareForRemove(entity)
        }
    }

    override suspend fun runTurns(callback: SceneEventsCallback?) {
        queue.forEach {
            if (it.type != EntityType.PLAYER) {
                if (it.shouldMove) {
                    it.nextTurnAction = aiRepo.getNextTurnAction(it, this@EntityManager, map)
                } else {
                    it.nextTurnAction = TurnAction.NOOP
                }
            }
            if (it.nextTurnAction.turnActionType == TurnActionType.MOVE) {
                move(it, callback)
            } else if (it.nextTurnAction.turnActionType == TurnActionType.ATTACK) {
                act(it, callback)
            } else if (it.nextTurnAction.turnActionType == TurnActionType.NONE) {
                Logger.log(Severity.VERBOSE, "Noop action")
            } else {
                Logger.log(Severity.ERROR, "Unhandled action!")
            }
        }
    }

    private suspend fun move(entity: GameEntityInterface, callback: SceneEventsCallback?): Boolean {
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

        if (map.isBlocked(x, y))
            return false

        if (isBlocked(x, y))
            return false

        setPosition(entity, x, y)
        doTileAction(x, y, callback)
        entity.nextTurnAction = TurnAction.NOOP

        callback?.onEntityChanged(entity)
        if (map.cellAt(x, y).terrain == TerrainType.END) {
            callback?.onSceneEnded(true)
        }

        return true
    }

    private suspend fun act(entity: GameEntityInterface, callback: SceneEventsCallback?): Boolean {
        var x = entity.posX
        var y = entity.posY

        when (entity.heading) {
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

        val targetEntity = getEntity(x, y)
        if (targetEntity != null) {
            if (targetEntity.group != entity.group) {
                doDamage(entity, targetEntity, callback)
            } else {
                doAction(targetEntity, callback)
            }
        } else {
            val cell = map.cellAt(x, y)
            cell.onActionTaken()
            callback?.onCellChanged(cell)
        }

        entity.nextTurnAction = TurnAction.NOOP
        return true
    }

    override fun setPosition(entity: GameEntityInterface, poxX: Int, posY: Int): Boolean {
        if (isBlocked(poxX, posY)) {
            return false
        }
        entityMap[entity.posX][entity.posY] = null
        entityMap[poxX][posY] = entity
        entity.posX = poxX
        entity.posY = posY
        return true
    }

    override fun prepareForAdd(entity: GameEntityInterface) {
        entity.enabled = true
        addSet.add(entity)
    }

    override fun prepareForRemove(entity: GameEntityInterface) {
        entity.enabled = false
        removeSet.add(entity)
        entityIdMap.remove(entity.id)
        entityMap[entity.posX][entity.posY] = null
        entitySet.remove(entity)
    }

    override fun doDamage(attacker: GameEntityInterface, defender: GameEntityInterface, callback: SceneEventsCallback?) {
        defender.health -= attacker.health
        if (defender.health <= 0) {
            defender.health = -1
            prepareForRemove(defender)
            callback?.onEntityChanged(defender)
        }
    }

    private suspend fun doAction(receiver: GameEntityInterface, callback: SceneEventsCallback?) {
        val trigger = entityTriggerMap.getValue(receiver.id)
        if (!trigger.enabled) {
            return
        }

        executeTrigger(trigger, callback)
    }

    private suspend fun doTileAction(posX: Int, posY: Int, callback: SceneEventsCallback?) {
        val trigger = triggerMap[posX][posY]
        if (trigger == null || !trigger.enabled) {
            return
        }

        executeTrigger(trigger, callback)
    }

    override fun handleSwapEntityEvent(event: SwapEntityEvent, callback: SceneEventsCallback?): BaseEvent? {
        val disableId = event.disableId
        val enableId = event.enableId

        val toDisableEntity = entityIdMap[disableId]
        var toEnableEntity: GameEntityInterface? = null
        toDisableEntity?.let { setEntityState(it, false) }
        disabledEntitySet.forEach {
            if (it.id == enableId) {
                setEntityState(it, true)
                toEnableEntity = it
            }
        }
        if (toEnableEntity != null && toDisableEntity != null) {
            toEnableEntity!!.posX = toDisableEntity.posX
            toEnableEntity!!.posY = toDisableEntity.posY
        }
        toDisableEntity?.let { callback?.onEntityChanged(it) }
        toEnableEntity?.let { callback?.onEntityChanged(it) }
        return eventMap[event.nextEventId]
    }

    private suspend fun handleInteractiveEntityEvent(event: InteractiveEvent): BaseEvent? {
        GlobalScope.launch(Dispatchers.Main) {
            eventListener?.onInteractionRequired(event.text, event.options, this@EntityManager)
        }
        val testEvent = channel.receive()
        return if (testEvent.type == EventType.NOOP) {
            null
        } else {
            testEvent
        }
    }

    override fun handleChangeTriggerEvent(event: ChangeTriggerEvent): BaseEvent? {
        val disableId = event.disableId
        val enableId = event.enableId

        triggerIdMap[enableId]?.enabled = true
        triggerIdMap[disableId]?.enabled = false

        return eventMap[event.nextEventId]
    }

    private suspend fun executeTrigger(trigger: Trigger, callback: SceneEventsCallback?) {
        if (nextEvent != null) {
            Logger.log(Severity.ERROR, "Cannot overwrite event")
        }

        nextEvent = eventMap[trigger.eventId]
        while (nextEvent != null) {
            var localNextEvent = nextEvent
            when (localNextEvent) {
                is SwapEntityEvent -> {
                    nextEvent = handleSwapEntityEvent(localNextEvent, callback)
                }
                is InteractiveEvent -> {
                    nextEvent = handleInteractiveEntityEvent(localNextEvent)
                }
                is ChangeTriggerEvent -> {
                    nextEvent = handleChangeTriggerEvent(localNextEvent)
                }
                else -> {
                }
            }
        }
    }
    override suspend fun selectOption(option: InteractiveEventOption?) {
        if (option == null) {
            channel.send(NoopEvent())
            return
        }

        if (option.eventId == InitialValues.INVALID_ID) {
            channel.send(NoopEvent())
            return
        }

        val eEvent = eventMap.getValue(option.eventId)
        channel.send(eEvent)
    }

    override fun processGameEntityState() {
        removeSet.forEach {
            deregister(it)
        }
        removeSet.clear()

        addSet.forEach {
            register(it)
        }
        addSet.clear()
    }

    override fun getEntity(posX: Int, posY: Int): GameEntityInterface? {
        return entityMap[posX][posY]
    }

    override fun isBlocked(posX: Int, posY: Int): Boolean {
        if (posX < 0 || posX > entityMap.lastIndex || posY < 0 || posY > entityMap.first().lastIndex)
            return true

        return entityMap[posX][posY] != null
    }
}