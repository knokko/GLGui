#version 140

in vec2 modelPosition;

out vec2 textureCoords;

uniform vec2 screenPosition;
uniform vec2 size;

void main(void){
	gl_Position = vec4(screenPosition.x + modelPosition.x * size.x, screenPosition.y + modelPosition.y * size.y, 0.0, 1.0);
	textureCoords = vec2(modelPosition.x, 1 - modelPosition.y);
}