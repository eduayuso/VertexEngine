package com.edosoft.vertexengine.editor.store

import com.edosoft.vertexengine.core.scene.SceneNode
import com.edosoft.vertexengine.editor.store.type.BaseAction

sealed class EditorAction: BaseAction {

    data class CreateNode(val isCube: Boolean) : EditorAction()

    data class SelectNode(val node: SceneNode?) : EditorAction()

    data class UpdateNodeName(val name: String) : EditorAction()

    object DuplicateNode : EditorAction()

    object DeleteNode : EditorAction()

    object UpdateTransform: EditorAction()

    object ToggleWireframe : EditorAction()
}