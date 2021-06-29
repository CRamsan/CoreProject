package com.cramsan.ps2link.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SwipeToRefresh(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onRefreshRequested: () -> Unit,
    content: LazyListScope.() -> Unit,
) {
    SwipeRefresh(
        modifier = modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { onRefreshRequested() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colors.primary,
                shape = CircleShape,
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@Composable
fun SwipeToRefreshColumn(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onRefreshRequested: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SwipeRefresh(
        modifier = modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { onRefreshRequested() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colors.primary,
                shape = CircleShape,
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            content = content
        )
    }
}
