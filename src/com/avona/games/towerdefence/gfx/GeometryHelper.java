package com.avona.games.towerdefence.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

public class GeometryHelper {
	public final static int FLOAT_SIZE = 4;
	public final static int CHAR_SIZE = 2;

	public static FloatBuffer allocateFloatBuffer(final int size) {
		return ByteBuffer.allocateDirect(FLOAT_SIZE * size).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
	}

	public static CharBuffer allocateCharBuffer(final int size) {
		return ByteBuffer.allocateDirect(CHAR_SIZE * size).order(
				ByteOrder.nativeOrder()).asCharBuffer();
	}

	public static CharBuffer allocateTriangleIndexBuffer(final int triangles) {
		return allocateCharBuffer(3 * triangles);
	}

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
	 * @param coordBuffer
	 *            Buffer into which the vertex coordinates are inserted.
	 * @param indexBuffer
	 *            Target buffer into which the vertices are inserted.
	 */
	public static void boxVerticesAsTriangles(final float x, final float y,
			final float width, final float height,
			final FloatBuffer coordBuffer, final CharBuffer indexBuffer) {
		final float left = x;
		final float right = x + width;
		final float bottom = y;
		final float top = y + height;

		final int baseIdx = coordBuffer.position() / 2;
		coordBuffer.put(left);
		coordBuffer.put(top);

		coordBuffer.put(right);
		coordBuffer.put(top);

		coordBuffer.put(left);
		coordBuffer.put(bottom);

		coordBuffer.put(right);
		coordBuffer.put(bottom);
		
		indexBuffer.put((char) (baseIdx + 0));
		indexBuffer.put((char) (baseIdx + 1));
		indexBuffer.put((char) (baseIdx + 2));

		indexBuffer.put((char) (baseIdx + 2));
		indexBuffer.put((char) (baseIdx + 1));
		indexBuffer.put((char) (baseIdx + 3));
	}
}
