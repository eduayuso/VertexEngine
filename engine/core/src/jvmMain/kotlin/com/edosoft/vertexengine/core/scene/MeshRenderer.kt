package com.edosoft.vertexengine.core.scene

import org.joml.Vector4f

class Material(
    val color: Vector4f = Vector4f(1f, 1f, 1f, 1f)
) {

    fun copy(): Material = Material(Vector4f(color))
}

enum class PrimitiveMesh {
    Cube
}

class MeshRenderer(
    val primitive: PrimitiveMesh = PrimitiveMesh.Cube,
    val material: Material = Material()
) : Component {

    override var owner: SceneNode? = null

    override fun copy(): MeshRenderer {

        return MeshRenderer(primitive, material.copy())
    }
}
