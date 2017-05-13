// Global
varying vec4 v_color;
uniform int level;

// Local
uniform vec2 physicalLocation;
uniform highp float clock;
varying float modifiedRadius;

const float pi = 3.14159;

void main(void) {
	vec2 posOnObject = mod( (gl_FragCoord.xy - physicalLocation) , modifiedRadius ) / modifiedRadius;
	//gl_FragColor = vec4(gl_FragCoord.xy, 1.0, 1.0) * v_color;

		//vec2 p = (gl_FragCoord.xy * 2.0 - resolution) / min(resolution.x, resolution.y);
		vec3 destColor = vec3(0.0, 0.5, 1.0);

		destColor += 0.01 / abs(length(posOnObject) - 0.8);

		gl_FragColor = vec4(destColor, 1.0);
}