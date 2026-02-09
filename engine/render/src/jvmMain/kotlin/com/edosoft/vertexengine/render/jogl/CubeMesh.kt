package com.edosoft.vertexengine.render.jogl

fun cubeMesh(): Pair<FloatArray, IntArray> {

    val v = floatArrayOf(
        -1f, -1f,  1f,  0f,  0f,  1f,
        1f, -1f,  1f,  0f,  0f,  1f,
        1f,  1f,  1f,  0f,  0f,  1f,
        -1f,  1f,  1f,  0f,  0f,  1f,

        -1f, -1f, -1f,  0f,  0f, -1f,
        -1f,  1f, -1f,  0f,  0f, -1f,
        1f,  1f, -1f,  0f,  0f, -1f,
        1f, -1f, -1f,  0f,  0f, -1f,

        -1f,  1f, -1f,  0f,  1f,  0f,
        -1f,  1f,  1f,  0f,  1f,  0f,
        1f,  1f,  1f,  0f,  1f,  0f,
        1f,  1f, -1f,  0f,  1f,  0f,

        -1f, -1f, -1f,  0f, -1f,  0f,
        1f, -1f, -1f,  0f, -1f,  0f,
        1f, -1f,  1f,  0f, -1f,  0f,
        -1f, -1f,  1f,  0f, -1f,  0f,

        1f, -1f, -1f,  1f,  0f,  0f,
        1f,  1f, -1f,  1f,  0f,  0f,
        1f,  1f,  1f,  1f,  0f,  0f,
        1f, -1f,  1f,  1f,  0f,  0f,

        -1f, -1f, -1f, -1f,  0f,  0f,
        -1f, -1f,  1f, -1f,  0f,  0f,
        -1f,  1f,  1f, -1f,  0f,  0f,
        -1f,  1f, -1f, -1f,  0f,  0f
    )

    val i = intArrayOf(
        0, 1, 2, 2, 3, 0,
        4, 5, 6, 6, 7, 4,
        8, 9, 10, 10, 11, 8,
        12, 13, 14, 14, 15, 12,
        16, 17, 18, 18, 19, 16,
        20, 21, 22, 22, 23, 20
    )

    return v to i
}
