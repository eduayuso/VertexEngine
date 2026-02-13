package com.edosoft.vertexengine.core.scene

interface Component {
    var owner: SceneNode?
    fun copy(): Component
}
