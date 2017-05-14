package com.android.texample2;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureRegion.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

class TextureRegion {
	float u1, v1; // Top left
	float u2, v2; // Bottom right

	/**
	 * calculate u and v coordinates from specified texture coordinates
	 *
	 * @param texWidth  width of texture region
	 * @param texHeight height of texture region
	 * @param x         left of region on texture (in pixels)
	 * @param y         top of region on texture (in pixels)
	 * @param width     width of region on texture (in pixels)
	 * @param height    height of region on texture (in pixels)
	 */
	TextureRegion(float texWidth, float texHeight, float x, float y, float width, float height) {
		this.u1 = x / texWidth;
		this.v1 = y / texHeight;
		this.u2 = this.u1 + (width / texWidth);
		this.v2 = this.v1 + (height / texHeight);
	}
}