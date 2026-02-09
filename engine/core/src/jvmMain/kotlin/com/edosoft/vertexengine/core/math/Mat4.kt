package com.edosoft.vertexengine.core.math

@JvmInline
value class Mat4f(val v: FloatArray) {

    init {
        require(v.size == 16)
    }

    companion object {

        fun identity(): Mat4f = Mat4f(floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        ))
    }
}
