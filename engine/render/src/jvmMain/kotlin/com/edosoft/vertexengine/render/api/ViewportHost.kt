package com.edosoft.vertexengine.render.api
import com.edosoft.vertexengine.core.scene.Scene

interface ViewportHost {

    val scene: Scene
    val controller: ViewportController
}
