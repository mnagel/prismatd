package com.android.texample2;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/AttribVariable.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

public enum AttribVariable {
	A_Position(1, "a_Position"),
	A_TexCoordinate(2, "a_TexCoordinate"),
	A_MVPMatrixIndex(3, "a_MVPMatrixIndex");

	private int mHandle;
	private String mName;

	private AttribVariable(int handle, String name) {
		mHandle = handle;
		mName = name;
	}

	public int getHandle() {
		return mHandle;
	}

	public String getName() {
		return mName;
	}
}