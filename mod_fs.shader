#version 330 core

in vec2 TexCoords;

out vec3 FragColor;

uniform sampler2D texture_diffuse;

void main() {
	FragColor = vec4(texture(texture_diffuse, TexCoords));
}