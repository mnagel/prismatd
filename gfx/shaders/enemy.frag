// Global
varying vec4 v_color;
uniform int level;

// Local
uniform vec2 physicalLocation;
uniform highp float clock;
varying float modifiedRadius;

const float pi = 3.14159;

mat2 rotate2d(float angle) {
	return mat2(
		+cos(angle), -sin(angle),
		+sin(angle), +cos(angle)
	);
}

void main( void ) {
		vec2 uv_1_1 = (gl_FragCoord.xy - physicalLocation) / modifiedRadius;
		uv_1_1 = rotate2d( sin(clock) * pi/4.0 ) * uv_1_1;
		float dist = max(abs(uv_1_1.x), abs(uv_1_1.y)); // infinity norm / squares
		dist *= 2;
		float intensity = 1.0 - dist;
		intensity = smoothstep(0.0, 1.0, intensity);
		gl_FragColor = vec4(vec3(v_color), intensity);
}
