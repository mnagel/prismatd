package com.avona.games.towerdefence;

import java.nio.FloatBuffer;

public class GeometryHelper {
	/**
	 * Constructs the vertices for a box in triangle strip form.
	 * 
	 * @param x
	 *            X-coordinate of the left corners of the box.
	 * @param y
	 *            Y-coordinate of the lower corners of the box.
	 * @param width
	 *            Width of the box.
	 * @param height
	 *            Height of the box.
	 * @param buffer
	 *            Target buffer into which the vertices are inserted.
	 */
	public static void boxVerticesAsTriangleStrip(final float x, final float y,
			final float width, final float height, final FloatBuffer buffer) {
		final float left = x;
		final float right = x + width;
		final float bottom = y;
		final float top = y + height;

		buffer.put(right);
		buffer.put(top);

		buffer.put(right);
		buffer.put(bottom);

		buffer.put(left);
		buffer.put(top);

		buffer.put(left);
		buffer.put(bottom);
	}

	public static void boxColoursAsTriangleStrip(final float colR,
			final float colG, final float colB, final float colA,
			final FloatBuffer buffer) {
		final float[] cols = new float[] { colR, colG, colB, colA };
		buffer.put(cols);
		buffer.put(cols);
		buffer.put(cols);
		buffer.put(cols);
	}
}
