package com.edosoft.vertexengine.editor.features.hierarchy

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edosoft.vertexengine.editor.store.EditorAction
import com.edosoft.vertexengine.editor.store.EditorState

@Composable
fun HierarchyPanel(
    state: EditorState,
    onAction: (EditorAction) -> Unit
) {

    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Hierarchy",
                    style = MaterialTheme.typography.titleSmall
                )

                Row {
                    var showCreateMenu by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { showCreateMenu = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)

                        DropdownMenu(
                            expanded = showCreateMenu,
                            onDismissRequest = { showCreateMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Empty Node") },
                                onClick = {
                                    onAction(
                                        EditorAction.CreateNode(false)
                                    )
                                    showCreateMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cube") },
                                onClick = {
                                    onAction(
                                        EditorAction.CreateNode(true)
                                    )
                                    showCreateMenu = false
                                }
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onAction(
                                EditorAction.DuplicateNode
                            )
                        },
                        enabled = state.selectedNode != null,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("D", style = MaterialTheme.typography.bodyMedium)
                    }

                    IconButton(
                        onClick = {
                            onAction(
                                EditorAction.DeleteNode
                            )
                        },
                        enabled = state.selectedNode != null && state.selectedNode.parent != null,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("X", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            key(state.sceneVersion) {

                HierarchyTree(
                    root = state.scene.root,
                    selectedNode = state.selectedNode
                ) { node ->
                    onAction(
                        EditorAction.SelectNode(node)
                    )
                }
            }
        }
    }
}