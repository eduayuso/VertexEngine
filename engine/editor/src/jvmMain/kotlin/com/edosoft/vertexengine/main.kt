package com.edosoft.vertexengine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.edosoft.vertexengine.editor.App

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "VertexEngine",
        state = WindowState(
            placement = WindowPlacement.Maximized
        )
    ) {
        App()
    }
}