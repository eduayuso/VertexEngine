package com.edosoft.vertexengine.render.jogl

import com.edosoft.vertexengine.core.scene.MeshRenderer
import com.edosoft.vertexengine.core.scene.PrimitiveMesh
import com.edosoft.vertexengine.core.scene.Scene
import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import org.joml.Matrix4f

class SceneRenderer(private val scene: Scene) : GLEventListener {

    private var program: Int = 0
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0
    private var indexCount: Int = 0

    private val mvpMatrix = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL3
        program = createProgram(gl, VERT_SHADER, FRAG_SHADER)

        val (vertices, indices) = cubeMesh()
        indexCount = indices.size

        val vaos = IntArray(1)
        gl.glGenVertexArrays(1, vaos, 0)
        vao = vaos[0]
        gl.glBindVertexArray(vao)

        val vbos = IntArray(1)
        gl.glGenBuffers(1, vbos, 0)
        vbo = vbos[0]
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo)
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (vertices.size * 4).toLong(), Buffers.newDirectFloatBuffer(vertices), GL.GL_STATIC_DRAW)

        val ebos = IntArray(1)
        gl.glGenBuffers(1, ebos, 0)
        ebo = ebos[0]
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebo)
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, (indices.size * 4).toLong(), Buffers.newDirectIntBuffer(indices), GL.GL_STATIC_DRAW)

        gl.glEnableVertexAttribArray(0)
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * 4, 0)
        gl.glEnableVertexAttribArray(1)
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * 4, 3 * 4)

        gl.glEnable(GL.GL_DEPTH_TEST)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {
        val gl = drawable.gl.gL3
        gl.glViewport(0, 0, w, h)
        projection.identity().perspective(Math.toRadians(45.0).toFloat(), w.toFloat() / h.toFloat(), 0.1f, 100f)
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL3
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)

        view.identity().lookAt(0f, 2f, 5f, 0f, 0f, 0f, 0f, 1f, 0f)

        scene.root.updateWorld()

        gl.glUseProgram(program)
        gl.glBindVertexArray(vao)

        val uMvp = gl.glGetUniformLocation(program, "uMvp")
        val uColor = gl.glGetUniformLocation(program, "uColor")

        scene.root.traverse { node ->
            val meshRenderer = node.getComponent(MeshRenderer::class.java)
            if (meshRenderer != null) {
                mvpMatrix.set(projection).mul(view).mul(node.worldMatrix)
                
                val mvpBuffer = FloatArray(16)
                mvpMatrix.get(mvpBuffer)
                gl.glUniformMatrix4fv(uMvp, 1, false, mvpBuffer, 0)

                val color = meshRenderer.material.color
                gl.glUniform4f(uColor, color.x, color.y, color.z, color.w)

                if (meshRenderer.primitive == PrimitiveMesh.Cube) {
                    gl.glDrawElements(GL.GL_TRIANGLES, indexCount, GL.GL_UNSIGNED_INT, 0)
                }
            }
        }
    }

    override fun dispose(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL3
        gl.glDeleteProgram(program)
        gl.glDeleteBuffers(1, intArrayOf(vbo), 0)
        gl.glDeleteBuffers(1, intArrayOf(ebo), 0)
        gl.glDeleteVertexArrays(1, intArrayOf(vao), 0)
    }

    private fun createProgram(gl: GL3, vsSrc: String, fsSrc: String): Int {
        val vs = compile(gl, GL3.GL_VERTEX_SHADER, vsSrc)
        val fs = compile(gl, GL3.GL_FRAGMENT_SHADER, fsSrc)
        val p = gl.glCreateProgram()
        gl.glAttachShader(p, vs)
        gl.glAttachShader(p, fs)
        gl.glLinkProgram(p)
        gl.glDetachShader(p, vs)
        gl.glDetachShader(p, fs)
        gl.glDeleteShader(vs)
        gl.glDeleteShader(fs)
        return p
    }

    private fun compile(gl: GL3, type: Int, src: String): Int {
        val s = gl.glCreateShader(type)
        gl.glShaderSource(s, 1, arrayOf(src), null)
        gl.glCompileShader(s)
        return s
    }
}
