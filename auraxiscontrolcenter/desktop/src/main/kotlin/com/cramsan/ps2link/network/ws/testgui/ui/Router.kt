package com.cramsan.ps2link.network.ws.testgui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

typealias RoutePath = RouteScope.() -> Unit
typealias PathContent = @Composable () -> Unit

@Composable
fun Root(
    path: Any?,
    content: @Composable RoutePath
) {
    var currentPath by remember(content) { mutableStateOf<PathContent?>(null) }
    val routeMap = remember(content) { mutableMapOf<Any, PathContent>() }
    val newPath = routeMap[path]

    currentPath = newPath

    val routes = remember(content) {
        object : RouteScope {
            override fun registerComposable(path: Any, content: PathContent) {
                routeMap[path] = content
            }
        }
    }
    var routesBuild by remember(content) { mutableStateOf(false) }
    if (!routesBuild) {
        routes.content()
        routesBuild = true
    }

    if (newPath != null) {
        newPath()
    }
}

interface RouteScope {
    fun registerComposable(
        path: Any,
        content: PathContent
    )
}

@Composable
fun RouteScope.Path(
    path: Any,
    content: PathContent
) {
    println("Registering $path = $content")
    registerComposable(path, content)
}
