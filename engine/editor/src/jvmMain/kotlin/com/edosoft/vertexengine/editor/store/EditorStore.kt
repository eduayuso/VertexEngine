package com.edosoft.vertexengine.editor.store

import com.edosoft.vertexengine.core.scene.Material
import com.edosoft.vertexengine.core.scene.MeshRenderer
import com.edosoft.vertexengine.core.scene.PrimitiveMesh
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.core.scene.SceneNode
import com.edosoft.vertexengine.editor.store.type.BaseStore

class EditorStore(initialScene: Scene) : BaseStore<EditorState, EditorAction>(
    EditorState(scene = initialScene)
) {

    override fun onAction(action: EditorAction) {

        when (action) {
            is EditorAction.SelectNode -> {
                selectNode(action)
            }
            is EditorAction.UpdateNodeName -> {
                updateNodeName(action)
            }
            EditorAction.DuplicateNode -> {
                duplicateNode()
            }
            EditorAction.DeleteNode -> {
                deleteNode()
            }

            is EditorAction.CreateNode -> {
                createNode(action)
            }

            EditorAction.UpdateTransform -> {
                updateTransform()
            }

            EditorAction.ToggleWireframe -> {
                setState {
                    state.copy(isWireframe = !state.isWireframe)
                }
            }
        }
    }

    private fun selectNode(action: EditorAction.SelectNode) {

        setState {
            state.copy(selectedNode = action.node)
        }
    }

    private fun updateNodeName(action: EditorAction.UpdateNodeName) {

        state.selectedNode?.let { node ->
            node.name = action.name
            state = state.copy(sceneVersion = state.sceneVersion + 1)
        }
    }

    private fun duplicateNode() {

        state.selectedNode?.let { node ->
            node.parent?.let { parent ->
                val copy = node.copy()
                copy.name = "${node.name} (Copy)"
                parent.addChild(copy)
                setState {
                    state.copy(sceneVersion = state.sceneVersion + 1)
                }
            }
        }
    }

    private fun deleteNode() {

        state.selectedNode?.let { node ->
            node.parent?.let { parent ->
                parent.removeChild(node)
                setState {
                    state.copy(
                        selectedNode = null,
                        sceneVersion = state.sceneVersion + 1
                    )
                }
            }
        }
    }

    private fun createNode(action: EditorAction.CreateNode) {

        val newNode = if (action.isCube) {
            SceneNode(name = "Cube").apply {
                addComponent(MeshRenderer(PrimitiveMesh.Cube, Material()))
            }
        } else {
            SceneNode(name = "Empty Node")
        }
        val parent = state.selectedNode ?: state.scene.root
        parent.addChild(newNode)
        setState {
            state.copy(sceneVersion = state.sceneVersion + 1)
        }
    }

    private fun updateTransform() {

        setState {
            state.copy(sceneVersion = state.sceneVersion + 1)
        }
    }
}