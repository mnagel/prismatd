package com.avona.games.towerdefence.gfx;

public class GeometryHelper {
	public final static int FLOAT_SIZE = 4;
	public final static int CHAR_SIZE = 2;

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
	 * @param va
	 *            Target buffer into which the vertices are inserted.
	 */
	public static void boxVerticesAsTriangleStrip(final float x, final float y,
			final float width, final float height, final VertexArray va) {
		final float left = x;
		final float right = x + width;
		final float bottom = y;
		final float top = y + height;

		va.addCoord(right, top);
		va.addCoord(right, bottom);
		va.addCoord(left, top);
		va.addCoord(left, bottom);
	}

	public static void boxColoursAsTriangleStrip(final float colR,
			final float colG, final float colB, final float colA,
			final VertexArray va) {
		final float[] cols = new float[] { colR, colG, colB, colA };

		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
	}

	/**
	 * Constructs the vertices for a box as triangles in vertex array form.
	 * 
	 * @param x
	 *            X-coordinate of the left corners of the box.
	 * @param y
	 *            Y-coordinate of the lower corners of the box.
	 * @param width
	 *            Width of the box.
	 * @param height
	 *            Height of the box.
	 * @param va
	 *            Array into which to insert the coordinates and indices.
	 */
	public static void boxVerticesAsTriangles(final float x, final float y,
			final float width, final float height, final VertexArray va) {
		final float left = x;
		final float right = x + width;
		final float bottom = y;
		final float top = y + height;

		va.addCoord(left, top);
		va.addCoord(right, top);
		va.addCoord(left, bottom);
		va.addCoord(right, bottom);

		va.addIndex(0);
		va.addIndex(1);
		va.addIndex(2);

		va.addIndex(2);
		va.addIndex(1);
		va.addIndex(3);
	}
}
