package com.avona.games.towerdefence.android.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureHelper.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.text.SpriteBatch;
import com.avona.games.towerdefence.gfx.text.Vertices;

public class AndroidSpriteBatch extends SpriteBatch {
	/**
	 * Prepare the sprite batcher for specified maximum number of sprites
	 *
	 * @param maxSprites maximum allowed sprites per batch
	 * @param shader     shader used for text rendering
	 */
	public AndroidSpriteBatch(int maxSprites, Shader shader) {
		super(maxSprites, shader);
	}

	@Override
	protected Vertices allocateVertices(Shader shader, int maxVertices, int maxIndices) {
		return new AndroidVertices(shader, maxVertices, maxIndices);
	}

	@Override
	protected void drawSprites() {
		GLES20.glUniformMatrix4fv(mvpMatricesHandle, numSprites, false, mvpMatrices, 0);
		GLES20.glEnableVertexAttribArray(mvpMatricesHandle);

		vertices.setVertices(vertexBuffer, 0, bufferIndex);
		vertices.draw(GLES20.GL_TRIANGLES, 0, numSprites * INDICES_PER_SPRITE);
	}
}
