package com.avona.games.towerdefence.gfx.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureHelper.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import com.avona.games.towerdefence.gfx.GlWrapper;
import com.avona.games.towerdefence.gfx.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

// TODO merge with VertexArray
public class Vertices {
	private static final int POSITION_CNT = 2;
	private static final int TEXCOORD_CNT = 2;
	private static final int MVP_MATRIX_INDEX_CNT = 1;
	private static final int INDEX_SIZE = Short.SIZE / 8;

	private final int texCoordinateHandle;
	private final int positionHandle;
	private final int mvpMatrixIndexHandle;
	private final int vertexSize;
	private final IntBuffer vertices;
	private final ShortBuffer indices;
	private final int[] verticesBuffer;
	private final GlWrapper glWrapper;

	/**
	 * Create the vertices/indices as specified
	 *
	 * @param shader      shader used for text rendering
	 * @param maxVertices maximum vertices allowed in buffer
	 * @param maxIndices  maximum indices allowed in buffer
	 */
	public Vertices(GlWrapper glWrapper, Shader shader, int maxVertices, int maxIndices) {
		this.glWrapper = glWrapper;
		int vertexStride = POSITION_CNT + TEXCOORD_CNT + MVP_MATRIX_INDEX_CNT;
		this.vertexSize = vertexStride * 4;

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		this.vertices = buffer.asIntBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * INDEX_SIZE);
			buffer.order(ByteOrder.nativeOrder());
			this.indices = buffer.asShortBuffer();
		} else {
			this.indices = null;
		}

		this.verticesBuffer = new int[maxVertices * vertexSize / 4];

		this.texCoordinateHandle = shader.getAttribLocation("a_texCoordinate");
		this.positionHandle = shader.getAttribLocation("a_position");
		this.mvpMatrixIndexHandle = shader.getAttribLocation("a_mvpMatrixIndex");
	}

	/**
	 * Set the specified vertices in the vertex buffer (optimized to use integer buffer)
	 *
	 * @param vertices array of vertices (floats) to set
	 * @param offset   offset to first vertex in array
	 * @param length   number of floats in the vertex array (total) for easy setting use: vtx_cnt * (this.vertexSize / 4)
	 */
	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int last = offset + length;
		for (int i = offset, j = 0; i < last; i++, j++) {
			verticesBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		}
		this.vertices.put(verticesBuffer, 0, length);
		this.vertices.flip();
	}

	/**
	 * Set the specified indices in the index buffer
	 *
	 * @param indices array of indices (shorts) to set
	 * @param offset  offset to first index in array
	 * @param length  number of indices in array (from offset)
	 */
	void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	/**
	 * Draw vertices into buffers
	 *
	 * @param primitiveType type of primitive to draw
	 * @param offset        offset in vertex / index buffer to start at
	 * @param numVertices   number of vertices (indices) to draw
	 */
	public void draw(int primitiveType, int offset, int numVertices) {
		// Perform all required binding/state changes before rendering batches
		vertices.position(0);
		glWrapper.glVertexAttribPointer(positionHandle, POSITION_CNT, glWrapper.GL_FLOAT, false, vertexSize, vertices);
		glWrapper.glEnableVertexAttribArray(positionHandle);

		vertices.position(POSITION_CNT);
		glWrapper.glVertexAttribPointer(texCoordinateHandle, TEXCOORD_CNT, glWrapper.GL_FLOAT, false, vertexSize, vertices);
		glWrapper.glEnableVertexAttribArray(texCoordinateHandle);

		vertices.position(POSITION_CNT + TEXCOORD_CNT);
		glWrapper.glVertexAttribPointer(mvpMatrixIndexHandle, MVP_MATRIX_INDEX_CNT, glWrapper.GL_FLOAT, false, vertexSize, vertices);
		glWrapper.glEnableVertexAttribArray(mvpMatrixIndexHandle);

		// Draw the currently bound vertices in the vertex/index buffers
		if (indices != null) {
			indices.position(offset);
			glWrapper.glDrawElements(primitiveType, numVertices, glWrapper.GL_UNSIGNED_SHORT, indices);
		} else {
			glWrapper.glDrawArrays(primitiveType, offset, numVertices);
		}

		// Clear binding states when done rendering batches
		glWrapper.glDisableVertexAttribArray(texCoordinateHandle);
	}
}