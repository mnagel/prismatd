package com.android.texample2;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/Vertices.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

class Vertices {
	private final static int POSITION_CNT = 2;              // Number of Components in Vertex Position for 2D
	private final static int TEXCOORD_CNT = 2;                 // Number of Components in Vertex Texture Coords
	private final static int INDEX_SIZE = Short.SIZE / 8;      // Index Byte Size (Short.SIZE = bits)
	private static final int MVP_MATRIX_INDEX_CNT = 1; // Number of Components in MVP matrix index

	private final int vertexSize;                       // Bytesize of a Single Vertex
	private final IntBuffer vertices;                          // Vertex Buffer
	private final ShortBuffer indices;                         // Index Buffer
	private final int[] tmpBuffer;                             // Temp Buffer for Vertex Conversion
	private int mTexCoordinateHandle;
	private int mPositionHandle;
	private int mMvpIndexHandle;

	//--Constructor--//
	// D: create the vertices/indices as specified (for 2d/3d)
	// A: maxVertices - maximum vertices allowed in buffer
	//    maxIndices - maximum indices allowed in buffer
	Vertices(int shaderProgramHandle, int maxVertices, int maxIndices) {
		int vertexStride = POSITION_CNT + TEXCOORD_CNT + MVP_MATRIX_INDEX_CNT;
		this.vertexSize = vertexStride * 4;        // Calculate Vertex Byte Size

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);  // Allocate Buffer for Vertices (Max)
		buffer.order(ByteOrder.nativeOrder());        // Set Native Byte Order
		this.vertices = buffer.asIntBuffer();           // Save Vertex Buffer

		if (maxIndices > 0) {                        // IF Indices Required
			buffer = ByteBuffer.allocateDirect(maxIndices * INDEX_SIZE);  // Allocate Buffer for Indices (MAX)
			buffer.order(ByteOrder.nativeOrder());     // Set Native Byte Order
			this.indices = buffer.asShortBuffer();       // Save Index Buffer
		} else                                            // ELSE Indices Not Required
		{
			indices = null;                              // No Index Buffer
		}

		this.tmpBuffer = new int[maxVertices * vertexSize / 4];  // Create Temp Buffer

		// initialize the shader attribute handles
		mTexCoordinateHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "a_texCoordinate");
		mPositionHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "a_position");
		mMvpIndexHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "a_mvpMatrixIndex");
	}

	//--Set Vertices--//
	// D: set the specified vertices in the vertex buffer
	//    NOTE: optimized to use integer buffer!
	// A: vertices - array of vertices (floats) to set
	//    offset - offset to first vertex in array
	//    length - number of floats in the vertex array (total)
	//             for easy setting use: vtx_cnt * (this.vertexSize / 4)
	void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();                          // Remove Existing Vertices
		int last = offset + length;                     // Calculate Last Element
		for (int i = offset, j = 0; i < last; i++, j++)  // FOR Each Specified Vertex
		{
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);  // Set Vertex as Raw Integer Bits in Buffer
		}
		this.vertices.put(tmpBuffer, 0, length);      // Set New Vertices
		this.vertices.flip();                           // Flip Vertex Buffer
	}

	//--Set Indices--//
	// D: set the specified indices in the index buffer
	// A: indices - array of indices (shorts) to set
	//    offset - offset to first index in array
	//    length - number of indices in array (from offset)
	void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();                           // Clear Existing Indices
		this.indices.put(indices, offset, length);    // Set New Indices
		this.indices.flip();                            // Flip Index Buffer
	}

	//--Bind--//
	// D: perform all required binding/state changes before rendering batches.
	//    USAGE: call once before calling draw() multiple times for this buffer.
	void bind() {
		// bind vertex position pointer
		vertices.position(0);                         // Set Vertex Buffer to Position
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// bind texture position pointer
		vertices.position(POSITION_CNT);  // Set Vertex Buffer to Texture Coords (NOTE: position based on whether color is also specified)
		GLES20.glVertexAttribPointer(mTexCoordinateHandle, TEXCOORD_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(mTexCoordinateHandle);

		// bind MVP Matrix index position handle
		vertices.position(POSITION_CNT + TEXCOORD_CNT);
		GLES20.glVertexAttribPointer(mMvpIndexHandle, MVP_MATRIX_INDEX_CNT, GLES20.GL_FLOAT, false, vertexSize, vertices);
		GLES20.glEnableVertexAttribArray(mMvpIndexHandle);
	}

	//--Draw--//
	// D: draw the currently bound vertices in the vertex/index buffers
	//    USAGE: can only be called after calling bind() for this buffer.
	// A: primitiveType - the type of primitive to draw
	//    offset - the offset in the vertex/index buffer to start at
	//    numVertices - the number of vertices (indices) to draw
	void draw(int primitiveType, int offset, int numVertices) {
		if (indices != null) {                       // IF Indices Exist
			indices.position(offset);                  // Set Index Buffer to Specified Offset
			//draw indexed
			GLES20.glDrawElements(primitiveType, numVertices,
					GLES20.GL_UNSIGNED_SHORT, indices);
		} else {                                         // ELSE No Indices Exist
			//draw direct
			GLES20.glDrawArrays(primitiveType, offset, numVertices);
		}
	}

	//--Unbind--//
	// D: clear binding states when done rendering batches.
	//    USAGE: call once before calling draw() multiple times for this buffer.
	void unbind() {
		GLES20.glDisableVertexAttribArray(mTexCoordinateHandle);
	}
}