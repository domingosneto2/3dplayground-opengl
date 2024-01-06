#version 330 core

// Vertex shader with Texture and Normal Map support.

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in vec3 aTangent;
layout (location = 4) in vec3 aBitangent;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 normal;

out VS_OUT {
    // Vertex position in world space.
    vec3 FragPos;
    // Vertex normal in world space.
    vec3 MyNormal;
    // Just passes through the texture coordinates
    vec2 TexCoord;
    // TBN matrix for texture mapping
    mat3 TBN;
} vs_out;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    vs_out.FragPos = vec3(model * vec4(aPos, 1));
    vs_out.MyNormal = normalize(vec3(normal * vec4(aNormal, 0)));
    //MyNormal = aNormal;
    vs_out.TexCoord = aTexCoord;
    vec3 T = normalize(vec3(model * vec4(aTangent,   0.0)));
    vec3 B = normalize(vec3(model * vec4(aBitangent, 0.0)));
    vec3 N = normalize(vec3(model * vec4(aNormal,    0.0)));
    vs_out.TBN = mat3(T, B, N);
}