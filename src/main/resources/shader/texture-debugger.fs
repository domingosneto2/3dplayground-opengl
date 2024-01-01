#version 330 core

// Fragment shader with basic Texture support.

in vec2 TexCoord;

uniform sampler2D texture1;

out vec4 FragColor;

void main()
{
    FragColor = texture(texture1, TexCoord);
}