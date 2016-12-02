package com.avona.games.towerdefence.gfx;

public abstract class Texture {
	/**
	 * Used by the graphics system to associate some hardware texture identifier
	 * with this texture object. The attribute is owned by the system-dependent
	 * graphics system.
	 */
	public int textureId;

	/**
	 * Width of the original texture in pixels.
	 */
	public int nativeWidth;
	/**
	 * Height of the original texture in pixels.
	 */
	public int nativeHeight;

	/**
	 * Actual texture width, including the padding necessary to reach a power of
	 * 2.
	 */
	public int width;
	/**
	 * Actual texture height, including the padding necessary to reach a power
	 * of 2.
	 */
	public int height;

	public float leftBorder;
	public float rightBorder;
	public float topBorder;
	public float lowerBorder;

	abstract public void loadImage(String fileName);
}
