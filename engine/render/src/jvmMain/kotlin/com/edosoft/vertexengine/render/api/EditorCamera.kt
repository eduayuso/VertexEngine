package com.edosoft.vertexengine.render.api

import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class EditorCamera {

    var yaw: Float = -90f
    var pitch: Float = -20f
    var distance: Float = 10f
    val target: Vector3f = Vector3f(0f, 0f, 0f)

    fun getViewMatrix(matrix: Matrix4f): Matrix4f {

        val x = distance * cos(Math.toRadians(pitch.toDouble())).toFloat() * cos(Math.toRadians(yaw.toDouble())).toFloat()
        val y = distance * sin(Math.toRadians(pitch.toDouble())).toFloat()
        val z = distance * cos(Math.toRadians(pitch.toDouble())).toFloat() * sin(Math.toRadians(yaw.toDouble())).toFloat()

        val position = Vector3f(x, y, z).add(target)
        return matrix.identity().lookAt(position, target, Vector3f(0f, 1f, 0f))
    }

    fun orbit(deltaYaw: Float, deltaPitch: Float) {

        yaw += deltaYaw
        pitch += deltaPitch
        pitch = pitch.coerceIn(-89f, 89f)
    }

    fun zoom(delta: Float) {

        distance -= delta
        if (distance < 0.1f) distance = 0.1f
    }

    fun pan(deltaX: Float, deltaY: Float) {

        // Cálculo simplificado de pan basado en la orientación actual
        val forward = Vector3f(target).sub(getPosition()).normalize()
        val right = Vector3f(forward).cross(0f, 1f, 0f).normalize()
        val up = Vector3f(right).cross(forward).normalize()

        target.add(right.mul(deltaX))
        target.add(up.mul(deltaY))
    }

    private fun getPosition(): Vector3f {

        val x = distance * cos(Math.toRadians(pitch.toDouble())).toFloat() * cos(Math.toRadians(yaw.toDouble())).toFloat()
        val y = distance * sin(Math.toRadians(pitch.toDouble())).toFloat()
        val z = distance * cos(Math.toRadians(pitch.toDouble())).toFloat() * sin(Math.toRadians(yaw.toDouble())).toFloat()
        return Vector3f(x, y, z).add(target)
    }
}