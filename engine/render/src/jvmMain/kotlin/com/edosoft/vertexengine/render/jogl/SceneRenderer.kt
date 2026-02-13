package com.edosoft.vertexengine.render.jogl

import com.edosoft.vertexengine.core.scene.MeshRenderer
import com.edosoft.vertexengine.core.scene.PrimitiveMesh
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.render.api.EditorCamera
import com.edosoft.vertexengine.render.api.ViewportController
import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL3
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import org.joml.Matrix4f
import org.joml.Vector3f

class SceneRenderer(
    private val scene: Scene,
    private val camera: EditorCamera,
    private val controller: ViewportController
) : GLEventListener {

    private var program: Int = 0
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0
    private var indexCount: Int = 0

    private val mvpMatrix = Matrix4f()
    private val modelMatrix = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    private val lightPos = Vector3f(5f, 10f, 5f)

    private var lineProgram: Int = 0

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL3
        program = createProgram(gl, VERT_SHADER, FRAG_SHADER)
        lineProgram = createProgram(gl, LINE_VERT_SHADER, LINE_FRAG_SHADER)

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
        gl.glEnable(GL.GL_CULL_FACE)
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

        camera.getViewMatrix(view)

        drawGrid(gl)
        drawAxes(gl)

        scene.root.updateWorld()

        gl.glUseProgram(program)
        gl.glBindVertexArray(vao)

        val uMvp = gl.glGetUniformLocation(program, "uMvp")
        val uModel = gl.glGetUniformLocation(program, "uModel")
        val uColor = gl.glGetUniformLocation(program, "uColor")
        val uLightPos = gl.glGetUniformLocation(program, "uLightPos")
        val uHighlighted = gl.glGetUniformLocation(program, "uHighlighted")
        val uWireframe = gl.glGetUniformLocation(program, "uWireframe")

        gl.glUniform3f(uLightPos, lightPos.x, lightPos.y, lightPos.z)

        scene.root.traverse { node ->
            val meshRenderer = node.getComponent(MeshRenderer::class.java)
            if (meshRenderer != null) {
                modelMatrix.set(node.worldMatrix)
                mvpMatrix.set(projection).mul(view).mul(modelMatrix)
                
                val mvpBuffer = FloatArray(16)
                mvpMatrix.get(mvpBuffer)
                gl.glUniformMatrix4fv(uMvp, 1, false, mvpBuffer, 0)

                val modelBuffer = FloatArray(16)
                modelMatrix.get(modelBuffer)
                gl.glUniformMatrix4fv(uModel, 1, false, modelBuffer, 0)

                val color = meshRenderer.material.color
                gl.glUniform4f(uColor, color.x, color.y, color.z, color.w)

                val isHighlighted = node.id == controller.selectedNodeId
                gl.glUniform1i(uHighlighted, if (isHighlighted) 1 else 0)

                if (meshRenderer.primitive == PrimitiveMesh.Cube) {
                    if (controller.isWireframe) {
                        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_LINE)
                        gl.glUniform1i(uWireframe, 1)
                        gl.glDrawElements(GL.GL_TRIANGLES, indexCount, GL.GL_UNSIGNED_INT, 0)
                        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_FILL)
                        gl.glUniform1i(uWireframe, 0)
                    } else {
                        gl.glDrawElements(GL.GL_TRIANGLES, indexCount, GL.GL_UNSIGNED_INT, 0)
                    }
                }
            }
        }
    }

    override fun dispose(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL3
        gl.glDeleteProgram(program)
        gl.glDeleteProgram(lineProgram)
        gl.glDeleteBuffers(1, intArrayOf(vbo), 0)
        gl.glDeleteBuffers(1, intArrayOf(ebo), 0)
        gl.glDeleteVertexArrays(1, intArrayOf(vao), 0)
    }

    private fun drawGrid(gl: GL3) {
        gl.glUseProgram(lineProgram)
        val uMvp = gl.glGetUniformLocation(lineProgram, "uMvp")
        mvpMatrix.set(projection).mul(view)
        val mvpBuffer = FloatArray(16)
        mvpMatrix.get(mvpBuffer)
        gl.glUniformMatrix4fv(uMvp, 1, false, mvpBuffer, 0)

        val size = 10
        val step = 1f
        val vertices = mutableListOf<Float>()

        for (i in -size..size) {
            // Line along Z
            vertices.addAll(listOf(i.toFloat() * step, 0f, -size.toFloat() * step, 0.3f, 0.3f, 0.3f))
            vertices.addAll(listOf(i.toFloat() * step, 0f, size.toFloat() * step, 0.3f, 0.3f, 0.3f))

            // Line along X
            vertices.addAll(listOf(-size.toFloat() * step, 0f, i.toFloat() * step, 0.3f, 0.3f, 0.3f))
            vertices.addAll(listOf(size.toFloat() * step, 0f, i.toFloat() * step, 0.3f, 0.3f, 0.3f))
        }

        drawLines(gl, vertices.toFloatArray())
    }

    private fun drawAxes(gl: GL3) {
        gl.glUseProgram(lineProgram)
        val uMvp = gl.glGetUniformLocation(lineProgram, "uMvp")
        
        // Dibujamos los ejes en el origen, pero queremos que se vean siempre (desactivamos depth test momentáneamente)
        gl.glDisable(GL.GL_DEPTH_TEST)
        
        mvpMatrix.set(projection).mul(view)
        val mvpBuffer = FloatArray(16)
        mvpMatrix.get(mvpBuffer)
        gl.glUniformMatrix4fv(uMvp, 1, false, mvpBuffer, 0)

        val vertices = floatArrayOf(
            0f, 0f, 0f, 1f, 0f, 0f, // X - Rojo
            1f, 0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 0f, 1f, 0f, // Y - Verde
            0f, 1f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 0f, 1f, // Z - Azul
            0f, 0f, 1f, 0f, 0f, 1f
        )

        drawLines(gl, vertices)
        gl.glEnable(GL.GL_DEPTH_TEST)
    }

    private fun drawLines(gl: GL3, vertices: FloatArray) {
        val vaoArr = IntArray(1)
        val vboArr = IntArray(1)
        gl.glGenVertexArrays(1, vaoArr, 0)
        gl.glGenBuffers(1, vboArr, 0)

        gl.glBindVertexArray(vaoArr[0])
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboArr[0])
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (vertices.size * 4).toLong(), Buffers.newDirectFloatBuffer(vertices), GL3.GL_STREAM_DRAW)

        gl.glEnableVertexAttribArray(0)
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * 4, 0)
        gl.glEnableVertexAttribArray(1)
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * 4, 3 * 4)

        gl.glDrawArrays(GL.GL_LINES, 0, vertices.size / 6)

        gl.glDeleteBuffers(1, vboArr, 0)
        gl.glDeleteVertexArrays(1, vaoArr, 0)
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
