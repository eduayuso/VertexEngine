package com.edosoft.vertexengine.editor.features.hierarchy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.edosoft.vertexengine.core.scene.SceneNode

@Composable
fun HierarchyTree(
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