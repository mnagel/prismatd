// all pixels
uniform highp float clock;
uniform int level;
uniform vec2 physicalLocation;
uniform bool selected;

// this pixel
varying float modifiedRadius;
varying vec4 v_color;

// math
const float pi = 3.14159;

void main(void) {
	// uv in -1..1
	vec2 uv_1_1 = (gl_FragCoord.xy - physicalLocation) / modifiedRadius;

	float bubbles = 2.0 * level;
	float bubble_size = 6.0;
	float bubble_size_change = 0.4;
	vec3 bubble_color = vec3(v_color);

	float ring_size = 0.5;
	float ring_thick = 0.4;
	float ring_intensity = 1.2;
	vec3 ring_color = vec3(v_color);

	vec3 base_glow = vec3(0.2);

	// main output control
	float intensity = -0.3;

	// BUBBLES
	// forwards, still, backwards
	for (float clk = -clock; clk <= clock; clk += clock) {
		// no still
		if (clk == 0) continue;

		for (float bubble = 0.0; bubble < bubbles; bubble++) {
			// angle depends on clock and bubble_id
			float ang = clk+sin(bubble+clock)*2.0*pi/bubbles;
			// position of bubble
			vec2 bubble_position = 0.5 * vec2(sin(ang), cos(ang));

			float dist = length(distance(uv_1_1, bubble_position)) * 6.0;
			// fluctuation of "dist"
			dist *= 1 + 0.4 * sin(ang + sin(clock));
			// look good
			dist = smoothstep(0.0, 1.0, dist);
			// invert and accumulate
			intensity += 1-dist;
		}
	}

	// RING
	float t = smoothstep(ring_size-ring_thick, ring_size, length(uv_1_1)) - smoothstep(ring_size, ring_size+ring_thick, length(uv_1_1));

	// mix bubble/ring color, add some base glow
	vec3 color = mix(bubble_color, ring_color, t/(intensity+t)) + base_glow ;
	intensity += ring_intensity * t;

	gl_FragColor = vec4(clamp(intensity * vec3(color), vec3(0.0), vec3(1.0)), intensity);
}
