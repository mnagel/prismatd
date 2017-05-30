package com.avona.games.towerdefence.gfx.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/SpriteBatch.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.Matrix;
import com.avona.games.towerdefence.gfx.GlWrapper;
import com.avona.games.towerdefence.gfx.Shader;

public class SpriteBatch {
	private final static int INDICES_PER_SPRITE = 6;
	private final static int VERTEX_SIZE = 5; // Vertex Size (in Components) ie. (X,Y,U,V,M), M is MVP matrix index
	private final static int VERTICES_PER_SPRITE = 4;

	private final float[] mvpMatrices = new float[DisplayText.CHAR_BATCH_SIZE * 16];
	private final float[] mvpMatrix = new float[16];
	private final int maxSprites;
	private final GlWrapper glWrapper;
	private final float[] vertexBuffer;
	private final Vertices vertices;
	private final int mvpMatricesHandle;
	private int bufferIndex;
	private int numSprites;
	private float[] vpMatrix;

	/**
	 * Prepare the sprite batcher for specified maximum number of sprites
	 *
	 * @param maxSprites maximum allowed sprites per batch
	 * @param shader     shader used for text rendering
	 */
	public SpriteBatch(GlWrapper glWrapper, int maxSprites, Shader shader) {
		this.glWrapper = glWrapper;
		this.maxSprites = maxSprites;
		this.vertexBuffer = new float[maxSprites * VERTICES_PER_SPRITE * VERTEX_SIZE];
		this.vertices = new Vertices(glWrapper, shader, maxSprites * VERTICES_PER_SPRITE, maxSprites * INDICES_PER_SPRITE);
		short[] indices = new short[maxSprites * INDICES_PER_SPRITE];
		short j = 0;
		for (int i = 0; i < indices.length; i += INDICES_PER_SPRITE, j += VERTICES_PER_SPRITE) {
			indices[i] = j;
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = j;
		}
		this.vertices.setIndices(indices, 0, indices.length);
		this.mvpMatricesHandle = shader.getUniformLocation("u_mvpMatrices");

		this.bufferIndex = 0;
		this.numSprites = 0;
	}

	void beginBatch(float[] vpMatrix) {
		numSprites = 0;
		bufferIndex = 0;
		this.vpMatrix = vpMatrix;
	}

	/**
	 * Signal the end of a batch. Render the batched sprites.
	 */
	void endBatch() {
		if (numSprites == 0) {
			return;
		}

		drawSprites();
	}

	private void drawSprites() {
		glWrapper.glUniformMatrix4fv(mvpMatricesHandle, numSprites, false, mvpMatrices, 0);
		glWrapper.glEnableVertexAttribArray(mvpMatricesHandle);

		vertices.setVertices(vertexBuffer, 0, bufferIndex);
		vertices.draw(glWrapper.GL_TRIANGLES, 0, numSprites * INDICES_PER_SPRITE);
	}

	/**
	 * Batch specified sprite to batch. Adds vertices for sprite to vertex buffer.
	 * NOTE: MUST be called after beginBatch() and before endBatch()
	 * NOTE if the batch overflows, this will render the current batch, restart it, then batch this sprite.
	 *
	 * @param x           position of sprite (center)
	 * @param y           position of sprite (center)
	 * @param width       width of sprite
	 * @param height      height of sprite
	 * @param region      texture region to use for sprite
	 * @param modelMatrix model matrix to assign to sprite
	 */
	void drawSprite(float x, float y, float width, float height, TextureRegion region, float[] modelMatrix) {
		if (numSprites == maxSprites) {
			endBatch();
			numSprites = 0;
			bufferIndex = 0;
		}

		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		vertexBuffer[bufferIndex++] = x1;
		vertexBuffer[bufferIndex++] = y1;
		vertexBuffer[bufferIndex++] = region.u1;
		vertexBuffer[bufferIndex++] = region.v2;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2;
		vertexBuffer[bufferIndex++] = y1;
		vertexBuffer[bufferIndex++] = region.u2;
		vertexBuffer[bufferIndex++] = region.v2;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2;
		vertexBuffer[bufferIndex++] = y2;
		vertexBuffer[bufferIndex++] = region.u2;
		vertexBuffer[bufferIndex++] = region.v1;
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x1;
		vertexBuffer[bufferIndex++] = y2;
		vertexBuffer[bufferIndex++] = region.u1;
		vertexBuffer[bufferIndex++] = region.v1;
		vertexBuffer[bufferIndex++] = numSprites;

		Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0);
		System.arraycopy(mvpMatrix, 0, mvpMatrices, numSprites * 16, 16);

		numSprites++;
	}
}