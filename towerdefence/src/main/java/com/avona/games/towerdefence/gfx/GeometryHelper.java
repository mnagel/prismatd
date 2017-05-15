package com.avona.games.towerdefence.gfx;

public class GeometryHelper {
	public final static int FLOAT_SIZE = 4;
	public final static int CHAR_SIZE = 2;

	/**
	 * Constructs the vertices for a box in triangle strip form.
	 *
	 * @param x      X-coordinate of the left corners of the box.
	 * @param y      Y-coordinate of the lower corners of the box.
	 * @param width  Width of the box.
	 * @param height Height of the box.
	 * @param va     Target buffer into which the vertices are inserted.
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
		final float[] cols = new float[]{colR, colG, colB, colA};

		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
	}

	static final int COORD_COUNT_BOX_VERTICES_AS_TRIANGLES = 4;
	static final int INDEX_COUNT_BOX_VERTICES_AS_TRIANGLES = 6;

	/**
	 * Constructs the vertices for a box as triangles in vertex array form.
	 *
	 * @param x      X-coordinate of the left corners of the box.
	 * @param y      Y-coordinate of the lower corners of the box.
	 * @param width  Width of the box.
	 * @param height Height of the box.
	 * @param va     Array into which to insert the coordinates and indices.
	 */
	public static void boxVerticesAsTriangles(final float x, final float y,
											  final float width, final float height, final VertexArray va) {
		final float left = x;
		final float right = x + width;
		final float bottom = y;
		final float top = y + height;

		int a = va.addCoord(left, top);
		int b = va.addCoord(right, top);
		int c = va.addCoord(left, bottom);
		int d = va.addCoord(right, bottom);

		va.addIndex(a);
		va.addIndex(b);
		va.addIndex(c);

		va.addIndex(c);
		va.addIndex(b);
		va.addIndex(d);
	}

	public static void boxColoursAsTriangles(final float colR,
											 final float colG, final float colB, final float colA,
											 final VertexArray va) {
		final float[] cols = new float[]{colR, colG, colB, colA};

		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
		va.addColour(cols);
	}

	public static void boxTextureAsTriangleStrip(final VertexArray va) {
		// Top right
		va.addTextureCoord(va.texture.rightBorder, va.texture.topBorder);
		// Lower right
		va.addTextureCoord(va.texture.rightBorder, va.texture.lowerBorder);
		// Top left
		va.addTextureCoord(va.texture.leftBorder, va.texture.topBorder);
		// Lower left
		va.addTextureCoord(va.texture.leftBorder, va.texture.lowerBorder);
	}
}
