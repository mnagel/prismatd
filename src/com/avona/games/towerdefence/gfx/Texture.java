package com.avona.games.towerdefence.gfx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.avona.games.towerdefence.Util;

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

	/**
	 * Width of the texture contents in percent. [0,1]
	 */
	public float widthScale;
	/**
	 * Height of the texture contents in percent. [0,1]
	 */
	public float heightScale;

	abstract public void loadImage(String fileName);

	protected ByteBuffer resizeAndCopyToBuffer(byte[] data,
			final ImageColorFormat fmt) {
		assert nativeHeight * nativeWidth * 4 == data.length;

		width = Util.roundUpPower2(nativeWidth);
		height = Util.roundUpPower2(nativeHeight);

		widthScale = (float) nativeWidth / (float) width;
		heightScale = (float) nativeHeight / (float) height;

		ByteBuffer buf = ByteBuffer.allocateDirect(width * height * 4);
		buf.order(ByteOrder.nativeOrder());
		for (int row = nativeHeight - 1; row >= 0; --row) {
			for (int i = 0; i < nativeWidth; ++i) {
				buf.put(data[row * 4 * nativeHeight + i * 4 + fmt.redIndex]); // R
				buf.put(data[row * 4 * nativeHeight + i * 4 + fmt.greenIndex]); // G
				buf.put(data[row * 4 * nativeHeight + i * 4 + fmt.blueIndex]); // B
				buf.put(data[row * 4 * nativeHeight + i * 4 + fmt.alphaIndex]); // A
			}
			for (int i = 0; i < (width - nativeWidth) * 4; ++i) {
				buf.put((byte) 0);
			}
		}
		for (int row = 0; row < height - nativeHeight; ++row) {
			for (int i = 0; i < width * 4; ++i) {
				buf.put((byte) 0);
			}
		}
		buf.position(0);
		return buf;
	}
}
