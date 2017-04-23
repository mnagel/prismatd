precision mediump float;

// Global
varying vec4 v_color;

// Local
uniform vec2 physicalLocation;
varying float modifiedRadius;

void main(void) {
	// Relative position on object in [0, 1] x [0, 1]
	vec2 posOnObject = 0.5 + (gl_FragCoord.xy - physicalLocation) / (2.0 * modifiedRadius);

	// Color from vertex shader
	vec4 color = v_color;

	// Final color
	gl_FragColor = color;
}