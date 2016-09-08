#version 140

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColour[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);	
	
	vec3 totalDiffuse = vec3(0.0);
	int i = 0;
		
        vec3 unitLightVector = normalize(toLightVector);
        float nDotl = dot(unitNormal,unitLightVector);
        float brightness = max(nDotl,0.0);        

        totalDiffuse = brightness * lightColour[i];
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);	

	out_Color =  vec4(totalDiffuse,1.0) * textureColour;
}