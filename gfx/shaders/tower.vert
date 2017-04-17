uniform bool selected;
uniform float clock;
uniform vec2 virtualLocation;
uniform float physicalRadius;
varying float modifiedRadius;

void main(void) {
	gl_FrontColor = gl_Color;
	vec4 v = gl_Vertex;
	modifiedRadius = physicalRadius;
	if (selected) {
		float scalingFactor = 1.0 + 0.2 * abs(sin(4.0 * clock));
		modifiedRadius *= scalingFactor;
		v -= vec4(virtualLocation, 0.0, 0.0);
		v.x *= scalingFactor;
		v.y *= scalingFactor;
		v += vec4(virtualLocation, 0.0, 0.0);
	}
	gl_Position = gl_ModelViewProjectionMatrix * v;
}