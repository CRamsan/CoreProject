package com.cramsan.stranded.lib.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual class GameScope actual constructor(dispatcher: CoroutineDispatcher) {

    actual val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    actual fun endScope() = scope.cancel()
}
