package com.edosoft.vertexengine.core.scene

import org.joml.Vector4f

class Material(
    val color: Vector4f = Vector4f(1f, 1f, 1f, 1f)
)

enum class PrimitiveMesh {
    Cube
}

class MeshRenderer(
    val primitive: PrimitiveMesh = PrimitiveMesh.Cube,
    val material: Material = Material()
) : Component {
    override var owner: SceneNode? = null
}
