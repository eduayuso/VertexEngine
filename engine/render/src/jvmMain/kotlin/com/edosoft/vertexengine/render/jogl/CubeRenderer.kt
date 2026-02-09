package com.edosoft.vertexengine.render.jogl

import com.edosoft.vertexengine.core.samples.RotatingCubeScene
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import org.joml.Matrix4f
import java.nio.FloatBuffer
import java.nio.IntBuffer

internal class CubeRenderer(
    private val demo: RotatingCubeScene
) : GLEventListener {

    var isRotating: Boolean = true
    var speed: Float = 1f

    private var program = 0
    private var vao = 0
    private var vbo = 0
    private var ebo = 0
    private var uMvp = -1

    private var lastNanos = 0L
    private var width = 1
    private var height = 1

    private val proj = Matrix4f()
    private val view = Matrix4f()
    private val model = Matrix4f()
    private val mvp = Matrix4f()
    private val mvpArray = FloatArray(16)

    override fun init(drawable: GLAutoDrawable) {

        val gl = drawable.gl.gL3
        lastNanos = System.nanoTime()

        gl.glEnable(GL.GL_DEPTH_TEST)
        gl.glClearColor(0.08f, 0.09f, 0.11f, 1f)

        program = createProgram(gl, VERT_SHADER, FRAG_SHADER)
        uMvp = gl.glGetUniformLocation(program, "uMvp")

        val (cubeVertices, cubeIndices) = cubeMesh()

        val ids = IntArray(1)

        gl.glGenVertexArrays(1, ids, 0)
        vao = ids[0]
        gl.glBindVertexArray(vao)

        gl.glGenBuffers(1, ids, 0)
        vbo = ids[0]
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo)
        gl.glBufferData(
            GL.GL_ARRAY_BUFFER,
            cubeVertices.size.toLong() * 4L,
            FloatBuffer.wrap(cubeVertices),
            GL.GL_STATIC_DRAW
        )

        gl.glGenBuffers(1, ids, 0)
        ebo = ids[0]
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebo)
        gl.glBufferData(
            GL.GL_ELEMENT_ARRAY_BUFFER,
            cubeIndices.size.toLong() * 4L,
            IntBuffer.wrap(cubeIndices),
            GL.GL_STATIC_DRAW
        )

        val stride = 6 * 4
        gl.glEnableVertexAttribArray(0)
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, stride, 0L)

        gl.glEnableVertexAttribArray(1)
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, stride, (3 * 4).toLong())

        gl.glBindVertexArray(0)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {

        val gl = drawable.gl.gL3
        width = maxOf(1, w)
        height = maxOf(1, h)
        gl.glViewport(0, 0, width, height)
    }

    override fun display(drawable: GLAutoDrawable) {

        val gl = drawable.gl.gL3

        val now = System.nanoTime()
        val dtSeconds = ((now - lastNanos).coerceAtMost(50_000_000L)) / 1_000_000_000.0f
        lastNanos = now

        demo.update(dtSeconds, speed, isRotating)

        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)
        gl.glUseProgram(program)

        val aspect = width.toFloat() / height.toFloat()

        proj.identity().perspective(1.0f, aspect, 0.1f, 100f)
        view.identity().lookAt(
            2.4f, 1.6f, 3.0f,
            0f, 0f, 0f,
            0f, 1f, 0f
        )

        model.set(demo.cube.worldMatrix)

        mvp.set(proj).mul(view).mul(model)
        mvp.get(mvpArray)

        gl.glUniformMatrix4fv(uMvp, 1, false, mvpArray, 0)

        gl.glBindVertexArray(vao)
        gl.glDrawElements(GL.GL_TRIANGLES, 36, GL.GL_UNSIGNED_INT, 0L)
        gl.glBindVertexArray(0)
    }

    override fun dispose(drawable: GLAutoDrawable) {

        val gl = drawable.gl.gL3
        val ids = IntArray(1)

        ids[0] = ebo
        gl.glDeleteBuffers(1, ids, 0)

        ids[0] = vbo
        gl.glDeleteBuffers(1, ids, 0)

        ids[0] = vao
        gl.glDeleteVertexArrays(1, ids, 0)

        gl.glDeleteProgram(program)
    }

    private fun createProgram(gl: GL3, vsSrc: String, fsSrc: String): Int {

        val vs = compile(gl, GL3.GL_VERTEX_SHADER, vsSrc)
        val fs = compile(gl, GL3.GL_FRAGMENT_SHADER, fsSrc)

        val prog = gl.glCreateProgram()
        gl.glAttachShader(prog, vs)
        gl.glAttachShader(prog, fs)
        gl.glLinkProgram(prog)

        val ok = IntArray(1)
        gl.glGetProgramiv(prog, GL3.GL_LINK_STATUS, ok, 0)
        if (ok[0] == 0) {
            val log = programLog(gl, prog)
            gl.glDeleteProgram(prog)
            throw IllegalStateException("Program link failed:\n$log")
        }

        gl.glDeleteShader(vs)
        gl.glDeleteShader(fs)
        return prog
    }

    private fun compile(gl: GL3, type: Int, src: String): Int {

        val shader = gl.glCreateShader(type)
        gl.glShaderSource(shader, 1, arrayOf(src), intArrayOf(src.length), 0)
        gl.glCompileShader(shader)

        val ok = IntArray(1)
        gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, ok, 0)
        if (ok[0] == 0) {
            val log = shaderLog(gl, shader)
            gl.glDeleteShader(shader)
            throw IllegalStateException("Shader compile failed:\n$log")
        }
        return shader
    }

    private fun shaderLog(gl: GL3, shader: Int): String {

        val len = IntArray(1)
        gl.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, len, 0)
        if (len[0] <= 1) return ""
        val buf = ByteArray(len[0])
        gl.glGetShaderInfoLog(shader, buf.size, null, 0, buf, 0)
        return String(buf)
    }

    private fun programLog(gl: GL3, program: Int): String {

        val len = IntArray(1)
        gl.glGetProgramiv(program, GL3.GL_INFO_LOG_LENGTH, len, 0)
        if (len[0] <= 1) return ""
        val buf = ByteArray(len[0])
        gl.glGetProgramInfoLog(program, buf.size, null, 0, buf, 0)
        return String(buf)
    }
}
