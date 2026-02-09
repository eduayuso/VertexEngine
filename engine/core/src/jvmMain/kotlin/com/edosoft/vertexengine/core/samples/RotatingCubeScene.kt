package com.edosoft.vertexengine.core.samples

import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.core.scene.SceneNode
import org.joml.Quaternionf

class RotatingCubeScene {

    val scene = Scene()
    val cube = SceneNode(name = "Cube")

    private var angleRad = 0f
    private val tmpRot = Quaternionf()

    init {
        scene.root.addChild(cube)
        cube.transform.position.set(0f, 0f, 0f)
        cube.transform.scale.set(1f, 1f, 1f)
    }

    fun update(dtSeconds: Float, speed: Float, isRotating: Boolean) {

        if (isRotating) angleRad += dtSeconds * speed
        tmpRot.identity().rotateY(angleRad)
        cube.transform.rotation.set(tmpRot)
        scene.root.updateWorld(null)
    }
}