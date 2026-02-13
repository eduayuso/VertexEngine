package com.edosoft.vertexengine.render.jogl

const val VERT_SHADER = """
#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;

uniform mat4 uMvp;
uniform mat4 uModel;
uniform vec4 uColor;

out vec3 vN;
out vec3 vWorldPos;
out vec4 vColor;

void main() {
    vN = mat3(transpose(inverse(uModel))) * aNormal;
    vWorldPos = vec3(uModel * vec4(aPos, 1.0));
    vColor = uColor;
    gl_Position = uMvp * vec4(aPos, 1.0);
}
"""

const val FRAG_SHADER = """
#version 330 core
in vec3 vN;
in vec3 vWorldPos;
in vec4 vColor;

uniform vec3 uLightPos;
uniform vec3 uViewPos;
uniform bool uHighlighted;
uniform bool uWireframe;

out vec4 FragColor;

void main() {
    if (uWireframe) {
        FragColor = vec4(vColor.rgb * 1.5, 1.0);
        return;
    }

    vec3 normal = normalize(vN);
    vec3 lightDir = normalize(uLightPos - vWorldPos);
    
    // Lambert shading
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 ambient = 0.3 * vColor.rgb;
    vec3 diffuse = diff * vColor.rgb;
    
    vec3 finalColor = ambient + diffuse;
    
    if (uHighlighted) {
        finalColor = mix(finalColor, vec3(1.0, 0.5, 0.0), 0.4); // Naranja para resaltar
    }
    
    FragColor = vec4(finalColor, vColor.a);
}
"""

const val LINE_VERT_SHADER = """
#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;

uniform mat4 uMvp;
out vec3 vColor;

void main() {
    vColor = aColor;
    gl_Position = uMvp * vec4(aPos, 1.0);
}
"""

const val LINE_FRAG_SHADER = """
#version 330 core
in vec3 vColor;
out vec4 FragColor;

void main() {
    FragColor = vec4(vColor, 1.0);
}
"""
