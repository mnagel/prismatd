uniform bool selected;
uniform float clock;
uniform vec2 physicalLocation;
varying float modifiedRadius;

void main(void) {
	vec2 posOnObject = 0.5 + (gl_FragCoord.xy - physicalLocation) / (2.0 * modifiedRadius);
	gl_FragColor = gl_Color;
}