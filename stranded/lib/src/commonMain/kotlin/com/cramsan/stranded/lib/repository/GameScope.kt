package com.cramsan.stranded.lib.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

expect class GameScope(dispatcher: CoroutineDispatcher) {

    val scope: CoroutineScope

    fun endScope()
}
