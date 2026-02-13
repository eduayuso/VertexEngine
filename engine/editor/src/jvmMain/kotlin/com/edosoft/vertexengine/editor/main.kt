package com.edosoft.vertexengine.editor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "VertexEngine",
        state = WindowState(
         //   placement = WindowPlacement.Maximized
        )
    ) {
        App()
    }
}