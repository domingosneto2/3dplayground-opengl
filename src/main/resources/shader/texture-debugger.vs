#version 330 core

// Vertex shader with basic Texture support

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

uniform mat4 model;

out vec2 TexCoord;

void main()
{
    gl_Position = model * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}