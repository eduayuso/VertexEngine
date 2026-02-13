package com.edosoft.vertexengine.core.scene

import org.joml.Matrix4f
import java.util.UUID

class SceneNode(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "Node",
    val transform: Transform = Transform(),
    val components: MutableList<Component> = mutableListOf()
) {
    init {
        components.forEach { it.owner = this }
    }

    var parent: SceneNode? = null
        private set

    private val _children = mutableListOf<SceneNode>()
    val children: List<SceneNode> get() = _children

    val localMatrix: Matrix4f = Matrix4f()
    val worldMatrix: Matrix4f = Matrix4f()

    var payload: Any? = null

    fun addComponent(component: Component) {
        components.add(component)
        component.owner = this
    }

    fun removeComponent(component: Component) {
        if (components.remove(component)) {
            component.owner = null
        }
    }

    fun <T : Component> getComponent(type: Class<T>): T? {
        return components.filterIsInstance(type).firstOrNull()
    }

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

    fun copy(): SceneNode {
        val newNode = SceneNode(
            name = this.name,
            transform = this.transform.copy()
        )
        for (component in components) {
            newNode.addComponent(component.copy())
        }
        for (child in _children) {
            newNode.addChild(child.copy())
        }
        return newNode
    }
}
