package com.edosoft.vertexengine.render.jogl

const val VERT_SHADER = """
#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;

uniform mat4 uMvp;
uniform vec4 uColor;

out vec3 vN;
out vec4 vColor;

void main() {
    vN = aNormal;
    vColor = uColor;
    gl_Position = uMvp * vec4(aPos, 1.0);
}
"""

const val FRAG_SHADER = """
#version 330 core
in vec3 vN;
in vec4 vColor;
out vec4 FragColor;

void main() {
    float l = dot(normalize(vN), normalize(vec3(0.4, 0.8, 0.2))) * 0.5 + 0.5;
    FragColor = vec4(vColor.rgb * l, vColor.a);
}
"""
