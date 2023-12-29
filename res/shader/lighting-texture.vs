#version 330 core

// Vertex shader with basic Texture support

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 normal;

out vec3 FragPos;
out vec3 MyNormal;
out vec2 TexCoord;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    FragPos = vec3(model * vec4(aPos, 1));
    MyNormal = normalize(vec3(normal * vec4(aNormal, 0)));
    TexCoord = aTexCoord;
}