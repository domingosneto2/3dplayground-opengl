#version 330 core

uniform vec4 lightColor;

out vec4 FragColor;

void main()
{
    FragColor = clamp(vec4(lightColor.xyz, 1.0), 0.0, 1.0);
}