#version 330 core

in vec3 FragPos;
in vec3 MyNormal;

uniform vec3 color;
uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 ambientColor;

out vec4 FragColor;

void main()
{
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(MyNormal, lightDir), 0.0);
    vec3 diffuse =diff * lightColor;
    vec3 result = (ambientColor + diffuse) * color;
    FragColor = vec4(result, 1.0);
}