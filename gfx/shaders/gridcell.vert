// Global
uniform mat4 u_mvpMatrix;
attribute vec4 a_position;
attribute vec4 a_color;
varying vec4 v_color;

// Local
uniform bool selected;
uniform float clock;
uniform vec2 virtualLocation;
uniform float physicalRadius;
varying float modifiedRadius;

void main(void) {
	v_color = a_color;
	gl_Position = u_mvpMatrix * a_position;
	modifiedRadius = physicalRadius;
}