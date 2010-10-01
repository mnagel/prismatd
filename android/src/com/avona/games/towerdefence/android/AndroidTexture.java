package com.avona.games.towerdefence.android;

import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.avona.games.towerdefence.gfx.ImageColorFormat;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

public class AndroidTexture extends Texture {
	private GL10 gl;

	public AndroidTexture(GL10 gl) {
		this.gl = gl;
	}

	@Override
	public void loadImage(String fileName) {
		final InputStream is = ResourceResolverRegistry.getInstance()
				.getRawResource("drawable/" + fileName);

		final Bitmap bm = BitmapFactory.decodeStream(is);
		nativeWidth = bm.getWidth();
		nativeHeight = bm.getHeight();

		ByteBuffer nativeBuf = ByteBuffer.allocate(nativeWidth * nativeHeight
				* 4);
		bm.copyPixelsToBuffer(nativeBuf);
		bm.recycle();
		byte[] data = nativeBuf.array();
		ByteBuffer buf = resizeAndCopyToBuffer(data, ImageColorFormat.RGBA);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		assert gl.glGetError() == 0;
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, width, height, 0,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buf);
		assert gl.glGetError() == 0;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
}
