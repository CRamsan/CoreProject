package com.cramsan.ps2link.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Lazy column with pull to refresh.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefresh(
    isLoading: Boolean,
    onRefreshRequested: () -> Unit,
    content: LazyListScope.() -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(isLoading, onRefreshRequested)

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )

        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

/**
 * Column with pull to refresh.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefreshColumn(
    isLoading: Boolean,
    onRefreshRequested: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(isLoading, onRefreshRequested)

    Box(Modifier.pullRefresh(pullRefreshState)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )

        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
