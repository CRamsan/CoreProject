package com.cramsan.awsgame.subsystems

import java.util.*

class CallbackManager : IGameSubsystem {
    private var time = 0f
    private val queue: PriorityQueue<ExecutionBlockEvent>
    override fun OnGameLoad() {}
    override fun OnScreenLoad() {}
    override fun OnScreenClose() {}
    override fun OnGameClose() {}
    fun registerEventAtTime(futureTime: Float, block: ExecutionBlockInterface?) {
        queue.add(ExecutionBlockEvent(futureTime, block))
    }

    fun registerEventFromNow(waitTime: Float, block: ExecutionBlockInterface?) {
        registerEventAtTime(time + waitTime, block)
    }

    fun update(delta: Float) {
        time += delta
        for (i in queue.indices) {
            val nextBlock = queue.peek()
            // If the next block is expected to run in the future then
            // we know that we can stop checking for more events.
            if (nextBlock.time > time) {
                break
            }
            queue.poll()
            nextBlock.executeBlock()
        }
    }

    /**
     * This object will wrap the callback interface and the time it needs to be called.
     * The time is represents the game time when the block will be called.
     */
    private inner class ExecutionBlockEvent(val time: Float, private var block: ExecutionBlockInterface?) :
        Comparable<Any?> {
        fun executeBlock() {
            block!!.execute()
            block = null
        }

        override fun compareTo(other: Any?): Int {
            if (other!!.javaClass == ExecutionBlockEvent::class.java) {
                return java.lang.Float.compare(time, (other as ExecutionBlockEvent?)!!.time)
            }
            throw RuntimeException("Other object is not of type " + this.javaClass)
        }

    }

    interface ExecutionBlockInterface {
        fun execute()
    }

    init {
        queue = PriorityQueue()
    }
}