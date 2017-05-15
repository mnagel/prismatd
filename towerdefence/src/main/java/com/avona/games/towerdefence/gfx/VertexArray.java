package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.util.Util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

final public class VertexArray {
	public final static int FLOAT_SIZE = 4;
	public final static int CHAR_SIZE = 2;
	public int numCoords;
	public FloatBuffer coordBuffer;
	private int coordCount;
	public FloatBuffer colourBuffer;
	public boolean hasColour;
	public FloatBuffer textureBuffer;
	public Texture texture;
	public boolean hasTexture;
	public Shader shader;
	public boolean hasShader;
	public int numIndexes;
	public ByteBuffer indexByteBuffer;
	public CharBuffer indexBuffer;
	public Mode mode;
	private ByteBuffer coordByteBuffer;
	private ByteBuffer colourByteBuffer;
	private ByteBuffer textureByteBuffer;

	public void reserveBuffers() {
		final BufferCache bc = BufferCache.getInstance();
		coordByteBuffer = bc.retrieveBuffer(numCoords * FLOAT_SIZE * 2);
		coordBuffer = coordByteBuffer.asFloatBuffer();
		coordBuffer.position(0);
		coordCount = 0;
		if (hasColour) {
			colourByteBuffer = bc.retrieveBuffer(numCoords * FLOAT_SIZE * 4);
			colourBuffer = colourByteBuffer.asFloatBuffer();
			colourBuffer.position(0);
		}
		if (hasTexture) {
			textureByteBuffer = bc.retrieveBuffer(numCoords * FLOAT_SIZE * 2);
			textureBuffer = textureByteBuffer.asFloatBuffer();
			textureBuffer.position(0);
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
			coordCount = 0;
		}
		if (colourByteBuffer != null) {
			bc.storeBuffer(colourByteBuffer);
			colourByteBuffer = null;
			colourBuffer = null;
		}
		if (textureByteBuffer != null) {
			bc.storeBuffer(textureByteBuffer);
			textureByteBuffer = null;
			textureBuffer = null;
		}
		if (indexByteBuffer != null) {
			bc.storeBuffer(indexByteBuffer);
			indexByteBuffer = null;
			indexBuffer = null;
		}
	}

	public int addCoord(final float x, final float y) {
		coordBuffer.put(x);
		coordBuffer.put(y);
		return coordCount++;
	}

	public int addCoord(final double x, final double y) {
		coordBuffer.put((float) x);
		coordBuffer.put((float) y);
		return coordCount++;
	}

	public int addCoord(final V2 p) {
		coordBuffer.put(p.x);
		coordBuffer.put(p.y);
		return coordCount++;
	}

	// rotate the whole VertexArray.
	// algorithm and memory management are ***VERY*** bad
	// do it better soon ;)
	@Deprecated
	public void rotate(final V2 o, final float degrees) {
		for (int i = 0; i < numCoords; i++) {
			V2 unpack = new V2(coordBuffer.get(2 * i), coordBuffer.get(2 * i + 1));
			unpack.rotate(o, degrees);
			coordBuffer.put(2 * i, unpack.x);
			coordBuffer.put(2 * i + 1, unpack.y);
		}

	}

	public void addTextureCoord(final float tx, final float ty) {
		textureBuffer.put(tx);
		textureBuffer.put(ty);
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

	public enum Mode {
		TRIANGLE_FAN, TRIANGLE_STRIP, TRIANGLES, LINE_STRIP
	}
}
