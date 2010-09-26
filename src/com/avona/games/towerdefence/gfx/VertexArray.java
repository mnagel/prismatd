package com.avona.games.towerdefence.gfx;

import java.nio.CharBuffer;
import java.nio.FloatBuffer;

final public class VertexArray {
	public enum Mode {
		TRIANGLE_FAN,
		TRIANGLE_STRIP,
		TRIANGLES,
		LINE_STRIP
	}

	public FloatBuffer coordBuffer;
	public FloatBuffer colourBuffer;
	public CharBuffer indexBuffer;

	public boolean hasColour;

	public int numElements;

	public Mode mode;
}
