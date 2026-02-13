package com.edosoft.vertexengine.core.scene

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform(
    val position: Vector3f = Vector3f(),
    val rotation: Quaternionf = Quaternionf(),
    val scale: Vector3f = Vector3f(1f, 1f, 1f)
) {
    fun toMatrix(out: Matrix4f = Matrix4f()): Matrix4f {
        return out.identity()
            .translate(position)
            .rotate(rotation)
            .scale(scale)
    }

    fun setIdentity() {
        position.set(0f, 0f, 0f)
        rotation.identity()
        scale.set(1f, 1f, 1f)
    }

    fun copy(): Transform {
        return Transform(
            Vector3f(position),
            Quaternionf(rotation),
            Vector3f(scale)
        )
    }
}