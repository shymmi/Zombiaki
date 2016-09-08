/*
    LESSON 5 - SHADERY, KOLOROWANIE
*/
#version 400 core

in vec3 position; /*// wejście*/
in vec2 textureCoords; 

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec2 pass_textureCoords;

void main(void){
    
    gl_Position  = viewMatrix * projectionMatrix * transformationMatrix * vec4(position, 1.0);
   // colour = vec3(position.x + 0.5, 0.3, position.y + 0.5);*/
    pass_textureCoords = textureCoords;
}