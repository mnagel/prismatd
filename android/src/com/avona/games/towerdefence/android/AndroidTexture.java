package com.avona.games.towerdefence.android;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.avona.games.towerdefence.Util;
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

		final Bitmap nativeBitmap = BitmapFactory.decodeStream(is);
		nativeWidth = nativeBitmap.getWidth();
		nativeHeight = nativeBitmap.getHeight();

		width = Util.roundUpPower2(nativeWidth);
		height = Util.roundUpPower2(nativeHeight);
		height = width = Math.max(width, height);

		leftBorder = 0;
		rightBorder = (float) nativeWidth / (float) width;
		// Top and bottom reversed.
		topBorder = 0;
		lowerBorder = (float) nativeHeight / (float) height;

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		int[] pixels = new int[nativeWidth * nativeHeight];
		nativeBitmap.getPixels(pixels, 0, nativeWidth, 0, 0, nativeWidth,
				nativeHeight);
		nativeBitmap.recycle();
		bitmap.setPixels(pixels, 0, nativeWidth, 0, 0, nativeWidth,
				nativeHeight);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		assert gl.glGetError() == 0;
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		assert gl.glGetError() == 0;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
}
