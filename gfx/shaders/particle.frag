precision highp float;

// all pixels
uniform float clock;
uniform int level;
uniform vec2 physicalLocation;

// this pixel
varying float modifiedRadius;
varying vec4 v_color;

// shader gfx config
const float thickness = 0.275;


// math
const float pi = 3.14159;

void main(void) {
	// Relative position on object in [0, 1] x [0, 1]
	vec2 posOnObject = vec2(0.5) + 0.5 * (gl_FragCoord.xy - physicalLocation) / modifiedRadius;

    // more shader gfx config
    float tailcount = min(float(level), 3.0);
    float n = 55.0 * float(level);
    const float f1 = 5.0;
    const float f2 = 1.0;

    // decimate high values, add base offset towards white
	vec4 color = vec4(f1/n, f1/n, f1/n, 1.0) * v_color + vec4(f2/n, f2/n, f2/n, 1.0);

	// http://glslsandbox.com/e#40071.0
	float intensity = 0.;
	for (float i = 0.; i < n; i++) {
		float angle = i/n * 2.0 * pi;
		vec2 xy = posOnObject + vec2(0.25 * cos(angle) - 0.5, 0.25 * sin(angle) - 0.5);
		intensity += pow(1000000., (0.77 - length(xy) * 1.9) * (1. + thickness * fract(i/n*tailcount - clock))) / 80000.;
	}

	gl_FragColor = vec4(clamp(intensity * vec3(color), vec3(0.), vec3(1.)), intensity);
}