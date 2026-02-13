package com.edosoft.vertexengine.render.jogl
import com.edosoft.vertexengine.core.scene.Scene
import com.edosoft.vertexengine.render.api.EditorCamera
import com.edosoft.vertexengine.render.api.ViewportController
import com.edosoft.vertexengine.render.api.ViewportHost
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import java.awt.BorderLayout
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import javax.swing.JPanel
import javax.swing.SwingUtilities

fun createGlPanel(scene: Scene): JPanel = GlHostPanel(scene)

class GlHostPanel(initialScene: Scene) : JPanel(BorderLayout()), ViewportHost {

    override val scene: Scene = initialScene
    
    private val editorCamera = EditorCamera()

    override val controller: ViewportController = object : ViewportController {
        override val camera: EditorCamera = editorCamera
        override var rotationSpeed: Float = 1.0f
        override var selectedNodeId: String? = null
        override var isWireframe: Boolean = false
    }

    private val renderer = SceneRenderer(scene, editorCamera, controller)

    private val animator: Animator

    init {

        val profile = GLProfile.get(GLProfile.GL3)
        val caps = GLCapabilities(profile).apply {
            depthBits = 24
            doubleBuffered = true
        }

        val canvas = GLCanvas(caps)
        canvas.addGLEventListener(renderer)
        
        setupInput(canvas)

        add(canvas, BorderLayout.CENTER)

        animator = Animator(canvas)
        animator.start()
    }

    private fun setupInput(canvas: GLCanvas) {
        val mouseAdapter = object : MouseAdapter() {
            private var lastMousePos = Point()

            override fun mousePressed(e: MouseEvent) {
                lastMousePos = e.point
            }

            override fun mouseDragged(e: MouseEvent) {
                val deltaX = (e.x - lastMousePos.x).toFloat()
                val deltaY = (e.y - lastMousePos.y).toFloat()

                if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
                    // Orbit
                    editorCamera.orbit(-deltaX * controller.rotationSpeed * 0.5f, deltaY * controller.rotationSpeed * 0.5f)
                } else if (SwingUtilities.isLeftMouseButton(e) && e.isShiftDown) {
                    // Pan
                    editorCamera.pan(-deltaX * 0.01f, deltaY * 0.01f)
                }

                lastMousePos = e.point
            }

            override fun mouseWheelMoved(e: MouseWheelEvent) {
                editorCamera.zoom(e.preciseWheelRotation.toFloat() * 0.5f)
            }
        }

        canvas.addMouseListener(mouseAdapter)
        canvas.addMouseMotionListener(mouseAdapter)
        canvas.addMouseWheelListener(mouseAdapter)
    }
}

