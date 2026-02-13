package com.edosoft.vertexengine.editor.features.inspector

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.edosoft.vertexengine.editor.features.inspector.components.Vector3Input
import com.edosoft.vertexengine.editor.store.EditorAction
import com.edosoft.vertexengine.editor.store.EditorState

@Composable
fun InspectorPanel(
    state: EditorState,
    onAction: (EditorAction) -> Unit
) {

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
            Text("Inspector", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Wireframe Mode")
                Switch(
                    checked = state.isWireframe,
                    onCheckedChange = { onAction(EditorAction.ToggleWireframe) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            state.selectedNode?.let { node ->

                Text("Inspector", style = MaterialTheme.typography.titleMedium)
                
                OutlinedTextField(
                    value = node.name,
                    onValueChange = {
                        onAction(
                            EditorAction.UpdateNodeName(it)
                        )
                    },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text("ID: ${node.id.take(8)}...")

                Spacer(Modifier.height(8.dp))

                Vector3Input("Position", node.transform.position) {
                    onAction(
                        EditorAction.UpdateTransform
                    )
                }

                val eulerAngles = remember(node.id) {
                    node.transform.rotation.getEulerAnglesXYZ(org.joml.Vector3f()).mul(180f / Math.PI.toFloat())
                }
                LaunchedEffect(node.transform.rotation, node.id) {
                    node.transform.rotation.getEulerAnglesXYZ(eulerAngles).mul(180f / Math.PI.toFloat())
                }

                Vector3Input("Rotation (Euler)", eulerAngles) {
                    node.transform.rotation.rotationXYZ(
                        Math.toRadians(eulerAngles.x.toDouble()).toFloat(),
                        Math.toRadians(eulerAngles.y.toDouble()).toFloat(),
                        Math.toRadians(eulerAngles.z.toDouble()).toFloat()
                    )
                    onAction(EditorAction.UpdateTransform)
                }

                Vector3Input("Scale", node.transform.scale) {
                    onAction(
                        EditorAction.UpdateTransform
                    )
                }

            } ?: run {
                Text("Select a node to inspect", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}