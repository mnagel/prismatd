precision mediump float;

uniform sampler2D u_texture;
uniform vec4 u_color;
varying vec2 v_texCoordinate;

void main() {
   gl_FragColor = texture2D(u_texture, v_texCoordinate).w * u_color;
}