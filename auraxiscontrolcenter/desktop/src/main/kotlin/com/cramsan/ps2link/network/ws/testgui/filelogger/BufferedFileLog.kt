package com.cramsan.ps2link.network.ws.testgui.filelogger

import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.PlayerEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.Closeable
import java.io.File
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

class BufferedFileLog(
    path: String,
) : FileLog, Closeable {

    private val bufferedFile = File(path).bufferedWriter()
    private val messageQueue: Queue<PlayerEvent> = ArrayBlockingQueue(MAX_CAPACITY)
    private val queueFlow = MutableSharedFlow<PlayerEvent>(
        onBufferOverflow = BufferOverflow.SUSPEND,
    )

    private var counter = 0
    override fun getLines(lines: Int): List<PlayerEvent> {
        return messageQueue.toList()
    }

    override fun observeLines(): Flow<PlayerEvent> {
        return queueFlow
    }

    override suspend fun addLine(event: PlayerEvent) {
        bufferedFile.appendLine(event.toString())
        messageQueue.add(event)
        queueFlow.emit(event)
        counter++
        if (counter >= 20) {
            bufferedFile.flush()
            counter = 0
        }
    }

    override fun close() {
        bufferedFile.flush()
        bufferedFile.close()
    }

    companion object {
        private const val MAX_CAPACITY = 100
    }
}
