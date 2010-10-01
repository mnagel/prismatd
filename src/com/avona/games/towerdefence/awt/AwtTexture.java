package com.avona.games.towerdefence.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import com.avona.games.towerdefence.gfx.ImageColorFormat;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

public class AwtTexture extends Texture {
	private GL gl;

	public AwtTexture(final GL gl) {
		this.gl = gl;
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
