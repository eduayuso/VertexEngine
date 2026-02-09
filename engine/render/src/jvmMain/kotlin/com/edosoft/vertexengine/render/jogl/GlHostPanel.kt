package com.edosoft.vertexengine.render.jogl
import com.edosoft.vertexengine.core.samples.RotatingCubeScene
import com.edosoft.vertexengine.render.api.ViewportController
import com.edosoft.vertexengine.render.api.ViewportHost
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import java.awt.BorderLayout
import javax.swing.JPanel

fun createGlPanel(): JPanel = GlHostPanel()

class GlHostPanel : JPanel(BorderLayout()), ViewportHost {

    private val demoScene = RotatingCubeScene()
    private val renderer = CubeRenderer(demoScene)

    override val controller: ViewportController = object : ViewportController {

        override var isRotating: Boolean
            get() = renderer.isRotating
            set(value) { renderer.isRotating = value }

        override var speed: Float
            get() = renderer.speed
            set(value) { renderer.speed = value }
    }

    private val animator: Animator

    init {
        val profile = GLProfile.get(GLProfile.GL3)
        val caps = GLCapabilities(profile).apply {
            depthBits = 24
            doubleBuffered = true
        }

        val canvas = GLCanvas(caps)
        canvas.addGLEventListener(renderer)

        add(canvas, BorderLayout.CENTER)

        animator = Animator(canvas)
        animator.start()
    }
}

