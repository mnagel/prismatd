package com.avona.games.towerdefence.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

import java.io.InputStream;

public class AndroidTexture extends Texture {
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

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}
}
