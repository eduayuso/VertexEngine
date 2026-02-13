package com.edosoft.vertexengine.editor.store

import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.core.scene.SceneNode
import com.edosoft.vertexengine.editor.store.type.BaseState

data class EditorState(

    val scene: Scene,
    val selectedNode: SceneNode? = null,
    val sceneVersion: Int = 0,
    val isWireframe: Boolean = false

): BaseState