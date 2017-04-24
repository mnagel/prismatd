precision highp float;

// Global
varying vec4 v_color;
uniform int level;

// Local
uniform vec2 physicalLocation;
uniform float clock;
varying float modifiedRadius;

const float pi = 3.14159;

void main(void) {
	// Relative position on object in [0, 1] x [0, 1]
	vec2 posOnObject = vec2(0.5) + 0.5 * (gl_FragCoord.xy - physicalLocation) / modifiedRadius;

    const float n = 50;
    const float f1 = 5.0;
    const float f2 = 1.0;

    // decimate high values, add base offset towards white
	vec4 color = vec4(f1/n, f1/n, f1/n, 1.0) * v_color + vec4(f2/n, f2/n, f2/n, 1.0);

	// http://glslsandbox.com/e#40071.0
	float intensity = 0.;
	for (float i = 0.; i < 54.; i++) {
		float angle = i/27. * pi;
		vec2 xy = posOnObject + vec2(0.25 * cos(angle) - 0.5, 0.25 * sin(angle) - 0.5);
		intensity += pow(1000000., (0.77 - length(xy) * 1.9) * (1. + 0.275 * fract(-i / 17. - clock))) / 80000.;
	}

	gl_FragColor = vec4(clamp(intensity * vec3(color), vec3(0.), vec3(1.)), intensity);
}