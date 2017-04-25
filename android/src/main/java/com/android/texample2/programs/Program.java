package com.android.texample2.programs;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/programs/Program.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.GLES20;
import com.android.texample2.AttribVariable;
import com.android.texample2.Utilities;


public abstract class Program {

	private int programHandle;
	private int vertexShaderHandle;
	private int fragmentShaderHandle;
	private boolean mInitialized;

	public Program() {
		mInitialized = false;
	}

	public void init() {
		init(null, null, null);
	}

	public void init(String vertexShaderCode, String fragmentShaderCode, AttribVariable[] programVariables) {
		vertexShaderHandle = Utilities.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		fragmentShaderHandle = Utilities.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		programHandle = Utilities.createProgram(
				vertexShaderHandle, fragmentShaderHandle, programVariables);

		mInitialized = true;
	}

	public int getHandle() {
		return programHandle;
	}

	public void delete() {
		GLES20.glDeleteShader(vertexShaderHandle);
		GLES20.glDeleteShader(fragmentShaderHandle);
		GLES20.glDeleteProgram(programHandle);
		mInitialized = false;
	}

	public boolean initialized() {
		return mInitialized;
	}
}