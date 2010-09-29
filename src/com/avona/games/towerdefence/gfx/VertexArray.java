package com.avona.games.towerdefence.gfx;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import com.avona.games.towerdefence.V2;

final public class VertexArray {
	public enum Mode {
		TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, LINE_STRIP
	}

	public final static int FLOAT_SIZE = 4;
	public final static int CHAR_SIZE = 2;

	public int numCoords;
	private ByteBuffer coordByteBuffer;
	public FloatBuffer coordBuffer;

	private ByteBuffer colourByteBuffer;
	public FloatBuffer colourBuffer;
	public boolean hasColour;

	public int numIndexes;
	public ByteBuffer indexByteBuffer;
	public CharBuffer indexBuffer;

	public Mode mode;

	public void reserveBuffers() {
		final BufferCache bc = BufferCache.getInstance();
		coordByteBuffer = bc.retrieveBuffer(numCoords * FLOAT_SIZE * 2);
		coordBuffer = coordByteBuffer.asFloatBuffer();
		coordBuffer.position(0);
		if (hasColour) {
			colourByteBuffer = bc.retrieveBuffer(numCoords * FLOAT_SIZE * 4);
			colourBuffer = colourByteBuffer.asFloatBuffer();
			colourBuffer.position(0);
		}
		if (numIndexes > 0) {
			indexByteBuffer = bc.retrieveBuffer(numIndexes * CHAR_SIZE);
			indexBuffer = indexByteBuffer.asCharBuffer();
			indexBuffer.position(0);
		}
	}

	public void freeBuffers() {
		final BufferCache bc = BufferCache.getInstance();
		if (coordByteBuffer != null) {
			bc.storeBuffer(coordByteBuffer);
			coordByteBuffer = null;
			coordBuffer = null;
		}
		if (colourByteBuffer != null) {
			bc.storeBuffer(colourByteBuffer);
			colourByteBuffer = null;
			colourBuffer = null;
		}
		if (indexByteBuffer != null) {
			bc.storeBuffer(indexByteBuffer);
			indexByteBuffer = null;
			indexBuffer = null;
		}
	}

	public void addCoord(final float x, final float y) {
		coordBuffer.put(x);
		coordBuffer.put(y);
	}

	public void addCoord(final double x, final double y) {
		coordBuffer.put((float) x);
		coordBuffer.put((float) y);
	}

	public void addCoord(final V2 p) {
		coordBuffer.put(p.x);
		coordBuffer.put(p.y);
	}

	public void addColour(final float colR, final float colG, final float colB,
			final float colA) {
		colourBuffer.put(colR);
		colourBuffer.put(colG);
		colourBuffer.put(colB);
		colourBuffer.put(colA);
	}

	public void addColour(final float[] cols) {
		assert cols.length == 4;
		colourBuffer.put(cols);
	}

	public void addIndex(final int idx) {
		indexBuffer.put((char) idx);
	}
}
