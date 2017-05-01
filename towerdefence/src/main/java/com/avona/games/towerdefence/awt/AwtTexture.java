package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.gfx.ImageColorFormat;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.Util;
import com.jogamp.opengl.GL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AwtTexture extends Texture {
	private GL gl;

	public AwtTexture(final GL gl) {
		this.gl = gl;
	}

	// TODO Use a native method instead of this.
	protected ByteBuffer resizeAndCopyToBuffer(
			byte[] data,
			final ImageColorFormat fmt) {
		assert nativeHeight * nativeWidth * 4 == data.length;

		width = Util.roundUpPower2(nativeWidth);
		height = Util.roundUpPower2(nativeHeight);
		height = width = Math.max(width, height);

		leftBorder = 0;
		rightBorder = (float) nativeWidth / (float) width;
		lowerBorder = 0;
		topBorder = (float) nativeHeight / (float) height;

		ByteBuffer buf = ByteBuffer.allocateDirect(width * height * 4);
		buf.order(ByteOrder.nativeOrder());
		for (int row = nativeHeight - 1; row >= 0; --row) {
			for (int i = 0; i < nativeWidth; ++i) {
				buf.put(data[row * 4 * nativeWidth + i * 4 + fmt.redIndex]); // R
				buf.put(data[row * 4 * nativeWidth + i * 4 + fmt.greenIndex]); // G
				buf.put(data[row * 4 * nativeWidth + i * 4 + fmt.blueIndex]); // B
				buf.put(data[row * 4 * nativeWidth + i * 4 + fmt.alphaIndex]); // A
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

	@Override
	public void loadImage(String imageName) {
		final InputStream is = ResourceResolverRegistry.getInstance()
				.getRawResource(imageName + ".png");

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load image stream");
		}

		nativeWidth = bi.getWidth();
		nativeHeight = bi.getHeight();
		final byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		final ByteBuffer buf = resizeAndCopyToBuffer(data,
				ImageColorFormat.ABGR);

		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
		assert gl.glGetError() == 0;
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0,
				GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buf);
		assert gl.glGetError() == 0;
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	}
}
