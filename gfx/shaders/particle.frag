// all pixels
uniform highp float clock;
uniform int level;
uniform vec2 physicalLocation;

// this pixel
varying float modifiedRadius;
varying vec4 v_color;

void main(void) {
	// Relative position on object in [-1, 1] x [-1, 1]
	vec2 uv_1_1 = (gl_FragCoord.xy - physicalLocation) / modifiedRadius;
	float dist = length(uv_1_1);
	float intensity = 1.0 - dist;
	intensity = smoothstep(0.0, 1.0, intensity);
	vec3 color = vec3(v_color) + vec3(0.25);
	gl_FragColor = vec4(clamp(vec3(intensity * color), vec3(0.0), vec3(1.0)), intensity);
}
