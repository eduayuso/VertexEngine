package com.edosoft.vertexengine.core.scene

import org.joml.Matrix4f
import java.util.UUID

class SceneNode(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "Node",
    val transform: Transform = Transform()
) {
    var parent: SceneNode? = null
        private set

    private val _children = mutableListOf<SceneNode>()
    val children: List<SceneNode> get() = _children

    val localMatrix: Matrix4f = Matrix4f()
    val worldMatrix: Matrix4f = Matrix4f()

    var payload: Any? = null

    fun addChild(child: SceneNode) {

        if (child.parent === this) return
        child.parent?._children?.remove(child)
        child.parent = this
        _children.add(child)
    }

    fun removeChild(child: SceneNode) {

        if (_children.remove(child)) {
            child.parent = null
        }
    }

    fun updateWorld(parentWorld: Matrix4f? = null) {

        transform.toMatrix(localMatrix)

        if (parentWorld == null) {
            worldMatrix.set(localMatrix)
        } else {
            worldMatrix.set(parentWorld).mul(localMatrix)
        }

        for (c in _children) {
            c.updateWorld(worldMatrix)
        }
    }

    fun traverse(action: (SceneNode) -> Unit) {

        action(this)
        for (c in _children) c.traverse(action)
    }
}
