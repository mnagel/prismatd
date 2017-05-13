// Global
uniform mat4 u_mvpMatrix;
attribute vec4 a_position;
attribute vec4 a_color;
varying vec4 v_color;

// Local
uniform bool selected;
uniform highp float clock;
uniform vec2 virtualLocation;
uniform float physicalRadius;
varying float modifiedRadius;

void main(void) {
	// Color from color attribute
	v_color = a_color;

	// Position from vertex attribute
	vec4 v = a_position;

	// Modify size on selection
	modifiedRadius = physicalRadius;
	if (selected) {
		// Pulsate +20% in size
		float scalingFactor = 1.0 + 0.2 * abs(sin(4.0 * clock));

		// Provide updated size for fragment shader
		modifiedRadius *= scalingFactor;

		// Update vertex position
		v -= vec4(virtualLocation, 0.0, 0.0);
		v.x *= scalingFactor;
		v.y *= scalingFactor;
		v += vec4(virtualLocation, 0.0, 0.0);
	}

	// Final vertex position
	gl_Position = u_mvpMatrix * v;
}