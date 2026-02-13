package com.edosoft.vertexengine.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.editor.features.hierarchy.HierarchyPanel
import com.edosoft.vertexengine.editor.features.inspector.InspectorPanel
import com.edosoft.vertexengine.editor.features.viewport.ViewportPanel
import com.edosoft.vertexengine.editor.store.EditorStore

@Composable
fun App() {

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        MainScreen()
    }
}

@Composable
private fun MainScreen() {

    val store = remember {
        EditorStore(Scene.createDefault())
    }
    val state = store.state

    Row(Modifier.fillMaxSize()) {

        HierarchyPanel(
            state = state,
            onAction = store::onAction
        )

        ViewportPanel(state)

        InspectorPanel(
            state = state,
            onAction = store::onAction
        )
    }
}