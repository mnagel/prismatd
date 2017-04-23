precision highp float;

// Global
varying vec4 v_color;

// Local
uniform vec2 physicalLocation;
uniform float clock;
varying float modifiedRadius;

void main(void) {
	vec2 posOnObject = (1.0 / 2.0 / modifiedRadius) * (gl_FragCoord.xy - physicalLocation);

	// Color from vertex shader
	vec4 color = v_color;

    vec2 uv = posOnObject + vec2(0.3, 0); //gl_FragCoord.xy/100 - 0.5; // WTF 0.3 ???
	float intensity = 0.;
	// http://glslsandbox.com/e#40071.0
	for (float i = 0.; i < 54.; i++)
    	{
    		float angle = i/27. * 3.14159;
    		vec2 xy = uv + vec2(0.25 * cos(angle) - 0.25, 0.25 * sin(angle));
    		intensity += pow(1000000., (0.77 - length(xy) * 1.9) * (1. + 0.275 * fract(-i / 17. - clock))) / 80000.;
    	}
    	gl_FragColor = vec4(clamp(intensity * vec3(color), vec3(0.), vec3(1.)), intensity);

}