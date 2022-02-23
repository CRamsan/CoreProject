package com.cramsan.stranded.lib.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class GameScope constructor(dispatcher: CoroutineDispatcher) {

    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    fun endScope() = scope.cancel()
}
