package com.avona.games.towerdefence.android.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureHelper.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.text.Vertices;

// TODO merge with AndroidDisplay default vertices handling
public class AndroidVertices extends Vertices {
	/**
	 * Create the vertices/indices as specified
	 *
	 * @param shader      shader used for text rendering
	 * @param maxVertices maximum vertices allowed in buffer
	 * @param maxIndices  maximum indices allowed in buffer
	 */
	public AndroidVertices(Shader shader, int maxVertices, int maxIndices) {
		super(shader, maxVertices, maxIndices);
	}

	@Override
	public void draw(int primitiveType, int offset, int numVertices) {
		// Perform all required binding/state changes before rendering batches
		vertices.position(0);
		GLES20.glVertexAttribPointer(positionHandle, POSITION_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(positionHandle);

		vertices.position(POSITION_CNT);
		GLES20.glVertexAttribPointer(texCoordinateHandle, TEXCOORD_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(texCoordinateHandle);

		vertices.position(POSITION_CNT + TEXCOORD_CNT);
		GLES20.glVertexAttribPointer(mvpMatrixIndexHandle, MVP_MATRIX_INDEX_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(mvpMatrixIndexHandle);

		// Draw the currently bound vertices in the vertex/index buffers
		if (indices != null) {
			indices.position(offset);
			GLES20.glDrawElements(primitiveType, numVertices, GLES20.GL_UNSIGNED_SHORT, indices);
		} else {
			GLES20.glDrawArrays(primitiveType, offset, numVertices);
		}

		// Clear binding states when done rendering batches
		GLES20.glDisableVertexAttribArray(texCoordinateHandle);
	}
}
