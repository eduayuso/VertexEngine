package com.edosoft.vertexengine.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.core.scene.SceneNode
import com.edosoft.vertexengine.render.api.ViewportHost
import com.edosoft.vertexengine.render.jogl.createGlPanel

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
    var rotationSpeed by remember { mutableStateOf(1f) }
    var scene by remember { mutableStateOf<Scene?>(null) }
    var selectedNode by remember { mutableStateOf<SceneNode?>(null) }

    Row(Modifier.fillMaxSize()) {
        // Hierarchy Panel (Left)
        Surface(
            tonalElevation = 1.dp,
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
        ) {
            Column(Modifier.padding(8.dp)) {
                Text(
                    "Hierarchy",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                HorizontalDivider()
                scene?.let {
                    HierarchyTree(it.root, selectedNode) { node ->
                        selectedNode = node
                    }
                }
            }
        }

        // Viewport (Center)
        SwingPanel(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            factory = { 
                val panel = createGlPanel()
                scene = (panel as ViewportHost).scene
                panel
            },
            update = { panel ->
                val host = panel as ViewportHost
                host.controller.rotationSpeed = rotationSpeed
            }
        )

        // Inspector Panel (Right)
        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Toolbar", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { rotationSpeed = 1.0f }) { Text("Play") }
                    OutlinedButton(onClick = { rotationSpeed = 0.0f }) { Text("Pause") }
                }

                Text("Rotation Speed: ${"%.2f".format(rotationSpeed)}")
                Slider(
                    value = rotationSpeed,
                    onValueChange = { rotationSpeed = it },
                    valueRange = 0f..3f
                )
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                selectedNode?.let { node ->
                    Text("Inspector", style = MaterialTheme.typography.titleMedium)
                    Text("Name: ${node.name}")
                    Text("ID: ${node.id.take(8)}...")
                } ?: run {
                    Text("Select a node to inspect", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun HierarchyTree(
    root: SceneNode,
    selectedNode: SceneNode?,
    onNodeSelected: (SceneNode) -> Unit
) {
    LazyColumn {
        item {
            HierarchyNodeItem(root, 0, selectedNode, onNodeSelected)
        }
    }
}

@Composable
private fun HierarchyNodeItem(
    node: SceneNode,
    depth: Int,
    selectedNode: SceneNode?,
    onNodeSelected: (SceneNode) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val isSelected = node === selectedNode

    Column {
        Surface(
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNodeSelected(node) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = (depth * 16).dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
            ) {
                if (node.children.isNotEmpty()) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.size(24.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(if (expanded) "▼" else "▶", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    Spacer(modifier = Modifier.width(24.dp))
                }
                
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (expanded) {
            node.children.forEach { child ->
                HierarchyNodeItem(child, depth + 1, selectedNode, onNodeSelected)
            }
        }
    }
}