package com.edosoft.vertexengine.render.jogl
import com.edosoft.vertexengine.core.scene.*
import com.edosoft.vertexengine.render.api.ViewportController
import com.edosoft.vertexengine.render.api.ViewportHost
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import org.joml.Vector4f
import java.awt.BorderLayout
import javax.swing.JPanel

fun createGlPanel(): JPanel = GlHostPanel()

class GlHostPanel : JPanel(BorderLayout()), ViewportHost {

    override val scene = Scene().apply {
        val cube = SceneNode(name = "Cube")
        cube.addComponent(MeshRenderer(PrimitiveMesh.Cube, Material(Vector4f(0.2f, 0.6f, 1.0f, 1.0f))))
        root.addChild(cube)
    }
    
    private val renderer = SceneRenderer(scene)
    private var rotationAngle = 0f

    override val controller: ViewportController = object : ViewportController {
        override var rotationSpeed: Float = 1.0f
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
        
        // Simple rotation logic for now to keep the "live" feel
        canvas.addGLEventListener(object : com.jogamp.opengl.GLEventListener {
            override fun init(drawable: com.jogamp.opengl.GLAutoDrawable) {}
            override fun reshape(drawable: com.jogamp.opengl.GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {}
            override fun display(drawable: com.jogamp.opengl.GLAutoDrawable) {
                rotationAngle += 0.01f * controller.rotationSpeed
                scene.root.children.firstOrNull()?.transform?.rotation?.identity()?.rotateY(rotationAngle)
            }
            override fun dispose(drawable: com.jogamp.opengl.GLAutoDrawable) {}
        })

        add(canvas, BorderLayout.CENTER)

        animator = Animator(canvas)
        animator.start()
    }
}

