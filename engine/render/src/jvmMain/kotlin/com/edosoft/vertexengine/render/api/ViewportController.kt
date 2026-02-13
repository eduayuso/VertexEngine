package com.edosoft.vertexengine.render.api

interface ViewportController {
    val camera: EditorCamera
    var rotationSpeed: Float
    var selectedNodeId: String?
    var isWireframe: Boolean
}
