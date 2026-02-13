package com.edosoft.vertexengine.core.scene

import org.joml.Vector4f

class Scene(
    val root: SceneNode = SceneNode(name = "Root")
) {
    companion object {

        fun createDefault(): Scene = Scene().apply {

            val cube = SceneNode(name = "Cube")
            cube.addComponent(
                MeshRenderer(
                    primitive = PrimitiveMesh.Cube,
                    material = Material(Vector4f(0.2f, 0.6f, 1.0f, 1.0f))
                )
            )
            root.addChild(cube)
        }
    }
}