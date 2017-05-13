uniform mat4 u_mvpMatrices[24];
attribute float a_mvpMatrixIndex;
attribute vec4 a_position;
attribute vec2 a_texCoordinate;
varying vec2 v_texCoordinate;

void main() {
	v_texCoordinate = a_texCoordinate;
	gl_Position = u_mvpMatrices[int(a_mvpMatrixIndex)] * a_position;
}