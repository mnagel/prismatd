package com.avona.games.towerdefence.android.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureHelper.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.text.DisplayText;
import com.avona.games.towerdefence.gfx.text.SpriteBatch;
import com.avona.games.towerdefence.gfx.text.TextureRegion;

public class AndroidDisplayText extends DisplayText {
	private final AssetManager assetManager;
	private final int mShaderProgramHandle;
	private final int mColorHandle;
	private final int mTextureUniformHandle;
	private int textureId = -1;

	/**
	 * @param shader       shader used for text rendering
	 * @param assetManager Android asset manager
	 */
	public AndroidDisplayText(Shader shader, AssetManager assetManager) {
		super(shader);

		this.assetManager = assetManager;
		this.mShaderProgramHandle = shader.getProgram();
		this.mColorHandle = shader.getUniformLocation("u_color");
		this.mTextureUniformHandle = shader.getUniformLocation("u_texture");
	}

	@Override
	protected SpriteBatch allocateSpriteBatch(int maxSprites, Shader shader) {
		return new AndroidSpriteBatch(maxSprites, shader);
	}

	@Override
	protected boolean loadFont(String file, int size) {
		// load the font and setup paint instance for drawing
		Typeface tf = Typeface.createFromAsset(assetManager, file);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(size);
		paint.setColor(0xffffffff);
		paint.setTypeface(tf);

		// get font metrics
		Paint.FontMetrics fm = paint.getFontMetrics();
		fontHeight = (float) Math.ceil(Math.abs(fm.bottom) + Math.abs(fm.top));
		fontAscent = (float) Math.ceil(Math.abs(fm.ascent));
		fontDescent = (float) Math.ceil(Math.abs(fm.descent));

		// determine the width of each character and max width (including unknown character)
		char[] s = new char[2];
		charWidthMax = charHeight = 0;
		float[] w = new float[2];
		int cnt = 0;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			s[0] = c;
			paint.getTextWidths(s, 0, 1, w);
			charWidths[cnt] = w[0];
			if (charWidths[cnt] > charWidthMax) {
				charWidthMax = charWidths[cnt];
			}
			cnt++;
		}
		s[0] = CHAR_NONE;
		paint.getTextWidths(s, 0, 1, w);
		charWidths[cnt] = w[0];
		if (charWidths[cnt] > charWidthMax) {
			charWidthMax = charWidths[cnt];
		}

		// set character height to font height
		charHeight = fontHeight;

		// find the maximum size, validate, and setup cell sizes
		cellWidth = (int) charWidthMax + (2 * fontPadX);
		cellHeight = (int) charHeight + (2 * fontPadY);
		int maxSize = cellWidth > cellHeight ? cellWidth : cellHeight;
		if (maxSize < FONT_SIZE_MIN || maxSize > FONT_SIZE_MAX) {
			return false;
		}

		// set texture size based on max font size (width or height)
		// NOTE: these values are fixed, based on the defined characters. when
		// changing start/end characters (CHAR_START/CHAR_END) this will need adjustment too!
		int textureSize;
		if (maxSize <= 24) {
			textureSize = 256;
		} else if (maxSize <= 40) {
			textureSize = 512;
		} else if (maxSize <= 80) {
			textureSize = 1024;
		} else {
			textureSize = 2048;
		}

		// create an empty bitmap (alpha only, transparent background)
		Bitmap bitmap = Bitmap.createBitmap(textureSize, textureSize, Bitmap.Config.ALPHA_8);
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0x00000000);

		// render each of the characters to the canvas (ie. build the font map)
		float x = fontPadX;
		float y = (cellHeight - 1) - fontDescent - fontPadY;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			s[0] = c;
			canvas.drawText(s, 0, 1, x, y, paint);
			x += cellWidth;
			if ((x + cellWidth - fontPadX) > textureSize) {
				x = fontPadX;
				y += cellHeight;
			}
		}
		s[0] = CHAR_NONE;
		canvas.drawText(s, 0, 1, x, y, paint);

		// save the bitmap in a texture
		textureId = TextureHelper.loadTexture(bitmap);

		// setup the array of character texture regions
		x = 0;
		y = 0;
		for (int c = 0; c < CHAR_CNT; c++) {
			charRgn[c] = new TextureRegion(textureSize, textureSize, x, y, cellWidth - 1, cellHeight - 1);
			x += cellWidth;
			if (x + cellWidth > textureSize) {
				x = 0;
				y += cellHeight;
			}
		}

		return true;
	}

	@Override
	protected void postDraw() {
		GLES20.glDisableVertexAttribArray(mColorHandle);
	}

	@Override
	protected void preDraw(float red, float green, float blue, float alpha) {
		GLES20.glUseProgram(mShaderProgramHandle);

		// TODO: only alpha component works, text is always black #BUG
		float[] color = {red, green, blue, alpha};
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		GLES20.glEnableVertexAttribArray(mColorHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
	}
}
