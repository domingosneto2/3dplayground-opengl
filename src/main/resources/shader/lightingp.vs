#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 normal;

out VS_OUT {
    // Vertex position in world space.
    vec3 FragPos;
    // Vertex normal in world space.
    vec3 MyNormal;
} vs_out;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    vs_out.FragPos = vec3(model * vec4(aPos, 1));
    vs_out.MyNormal = normalize(vec3(normal * vec4(aNormal, 0)));
}