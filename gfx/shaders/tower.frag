precision highp float;

// Global
varying vec4 v_color;

// Local
uniform vec2 physicalLocation;
uniform float clock;
varying float modifiedRadius;

const float pi = 3.14159;

void main(void) {
	// Relative position on object in [0, 1] x [0, 1]
	vec2 posOnObject = vec2(0.5) + 0.5 * (gl_FragCoord.xy - physicalLocation) / modifiedRadius;

	// Color from vertex shader
	vec4 color = v_color;

	// http://glslsandbox.com/e#40071.0
	float intensity = 0.;
	for (float i = 0.; i < 54.; i++) {
		float angle = i/27. * pi;
		vec2 xy = posOnObject + vec2(0.25 * cos(angle) - 0.5, 0.25 * sin(angle) - 0.5);
		intensity += pow(1000000., (0.77 - length(xy) * 1.9) * (1. + 0.275 * fract(-i / 17. - clock))) / 80000.;
	}

	gl_FragColor = vec4(clamp(intensity * vec3(color), vec3(0.), vec3(1.)), intensity);
}