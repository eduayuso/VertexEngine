package com.edosoft.vertexengine.editor.features.viewport

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.render.api.ViewportHost
import com.edosoft.vertexengine.render.jogl.createGlPanel

@Composable
fun RowScope.ViewportPanel(
    state: com.edosoft.vertexengine.editor.store.EditorState,
) {

    SwingPanel(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        factory = {
            createGlPanel(state.scene)
        },
        update = { panel ->
            val host = panel as ViewportHost
            host.controller.selectedNodeId = state.selectedNode?.id
            host.controller.isWireframe = state.isWireframe
        }
    )
}