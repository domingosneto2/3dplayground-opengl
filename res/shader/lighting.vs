#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 normal;

uniform vec4 color;
uniform vec3 lightPos;
uniform vec4 lightColor;
uniform vec4 ambientColor;

out vec4 MyColor;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    vec3 vertPos = vec3(model * vec4(aPos, 1));
    vec3 norm = normalize(vec3(normal * vec4(aNormal, 0)));
    float lightDist = length(lightPos - vertPos);
    vec3 lightDir = normalize(lightPos - vertPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec4 diffuse =diff * lightColor * 1 / lightDist;
    vec4 result = (ambientColor + diffuse) * color;
    MyColor = vec4(result.xyz, 1.0);
}