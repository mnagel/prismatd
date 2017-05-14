package com.android.texample2;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/GLText.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.avona.games.towerdefence.gfx.Shader;

/**
 * This is a OpenGL ES 1.0 dynamic font rendering system. It loads actual font
 * files, generates a font map (texture) from them, and allows rendering of
 * text strings.
 * NOTE: the rendering portions of this class uses a sprite batcher in order
 * provide decent speed rendering. Also, rendering assumes a BOTTOM-LEFT
 * origin, and the (x,y) positions are relative to that, as well as the
 * bottom-left of the string to render.
 */
public class GLText {
	static final int CHAR_BATCH_SIZE = 24; // Number of characters to render per batch, must be the same as the size of u_mvpMatrices in shader
	private static final int CHAR_START = 32; // First character (ASCII)
	private static final int CHAR_END = 126; // Last character (ASCII)
	private static final int CHAR_CNT = (((CHAR_END - CHAR_START) + 1) + 1); // Character count (including character to use for unknown)
	private static final int CHAR_NONE = 32; // Character to use for unknown (ASCII)
	private static final int CHAR_UNKNOWN = (CHAR_CNT - 1); // Index of the unknown character
	private static final int FONT_SIZE_MIN = 6; // Minimum font size (pixels)
	private static final int FONT_SIZE_MAX = 180; // Maximum font size (pixels)
	private final float[] charWidths; // Width of each character (pixels)
	private AssetManager assets; // Asset manager
	private SpriteBatch batch; // Batch renderer
	private int fontPadX; // Horizontal font padding (pixels)
	private int fontPadY; // Vertical font padding (pixels)
	private float fontHeight; // Font height (pixels)
	private float fontAscent; // Font ascent (above baseline; pixels)
	private float fontDescent; // Font descent (below baseline; pixels)
	private int textureId; // Font texture ID
	private int textureSize; // Texture size for font (square)
	private float charWidthMax; // Character width (pixels)
	private float charHeight; // Character height (pixels)
	private TextureRegion[] charRgn; // Region of each character (texture coordinates)
	private int cellWidth, cellHeight; // Character cell width / height

	private float scaleX, scaleY; // Font scale
	private float spaceX; // Additional spacing (unscaled)

	private int mShaderProgramHandle;
	private int mColorHandle;
	private int mTextureUniformHandle;

	/**
	 * save program + asset manager, create arrays, and initialize the members
	 *
	 * @param shader shader used for text rendering
	 * @param assets asset manager
	 */
	public GLText(Shader shader, AssetManager assets) {
		this.assets = assets;
		this.mShaderProgramHandle = shader.getProgram();

		batch = new SpriteBatch(CHAR_BATCH_SIZE, shader);

		charWidths = new float[CHAR_CNT];
		charRgn = new TextureRegion[CHAR_CNT];

		fontPadX = 0;
		fontPadY = 0;

		fontHeight = 0.0f;
		fontAscent = 0.0f;
		fontDescent = 0.0f;

		textureId = -1;
		textureSize = 0;

		charWidthMax = 0;
		charHeight = 0;

		cellWidth = 0;
		cellHeight = 0;

		scaleX = 1.0f;
		scaleY = 1.0f;
		spaceX = 0.0f;

		mColorHandle = shader.getUniformLocation("u_color");
		mTextureUniformHandle = shader.getUniformLocation("u_texture");
	}

	/**
	 * Load the specified font file, create a texture for the defined character range, and setup all required values used to render with it.
	 *
	 * @param file filename of the font (.ttf, .otf) to use in assets
	 * @param size requested pixel size of font (height)
	 * @param padX extra padding per character to prevent overlapping characters.
	 * @param padY extra padding per character to prevent overlapping characters.
	 * @return success
	 */
	public boolean load(String file, int size, int padX, int padY) {
		fontPadX = padX;
		fontPadY = padY;

		// load the font and setup paint instance for drawing
		Typeface tf = Typeface.createFromAsset(assets, file);
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

	/**
	 * Begin with white opaque
	 *
	 * @param vpMatrix view and projection matrix to use
	 */
	public void begin(float[] vpMatrix) {
		begin(1.0f, 1.0f, 1.0f, 1.0f, vpMatrix);
	}

	/**
	 * Begin with white and explicit alpha
	 *
	 * @param alpha    explicit alpha value for font (default = 1.0)
	 * @param vpMatrix view and projection matrix to use
	 */
	public void begin(float alpha, float[] vpMatrix) {
		begin(1.0f, 1.0f, 1.0f, alpha, vpMatrix);
	}

	/**
	 * Begin with custom color
	 *
	 * @param red      RGB values for font (default = 1.0)
	 * @param green    RGB values for font (default = 1.0)
	 * @param blue     RGB values for font (default = 1.0)
	 * @param alpha    explicit alpha value for font (default = 1.0)
	 * @param vpMatrix view and projection matrix to use
	 */
	public void begin(float red, float green, float blue, float alpha, float[] vpMatrix) {
		initDraw(red, green, blue, alpha);
		batch.beginBatch(vpMatrix);
	}

	private void initDraw(float red, float green, float blue, float alpha) {
		GLES20.glUseProgram(mShaderProgramHandle);

		// TODO: only alpha component works, text is always black #BUG
		float[] color = {red, green, blue, alpha};
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		GLES20.glEnableVertexAttribArray(mColorHandle);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
	}

	public void end() {
		batch.endBatch();
		GLES20.glDisableVertexAttribArray(mColorHandle);
	}

	/**
	 * Draw text at the specified position
	 *
	 * @param text      string to draw
	 * @param x         position to draw text at (bottom left of text; including descent)
	 * @param y         position to draw text at (bottom left of text; including descent)
	 * @param z         position to draw text at (bottom left of text; including descent)
	 * @param angleDegX angle to rotate text
	 * @param angleDegY angle to rotate text
	 * @param angleDegZ angle to rotate text
	 */
	public void draw(String text, float x, float y, float z, float angleDegX, float angleDegY, float angleDegZ) {
		float chrHeight = cellHeight * scaleY;
		float chrWidth = cellWidth * scaleX;
		x += (chrWidth / 2.0f) - (fontPadX * scaleX);
		y += (chrHeight / 2.0f) - (fontPadY * scaleY);

		// Create a model matrix based on position and angle
		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, z);
		Matrix.rotateM(modelMatrix, 0, angleDegZ, 0, 0, 1);
		Matrix.rotateM(modelMatrix, 0, angleDegX, 1, 0, 0);
		Matrix.rotateM(modelMatrix, 0, angleDegY, 0, 1, 0);

		float letterX, letterY;
		letterX = letterY = 0;

		for (int i = 0; i < text.length(); i++) {
			int c = (int) text.charAt(i) - CHAR_START;
			if (c < 0 || c >= CHAR_CNT) {
				c = CHAR_UNKNOWN;
			}
			//TODO: optimize - applying the same model matrix to all the characters in the string
			batch.drawSprite(letterX, letterY, chrWidth, chrHeight, charRgn[c], modelMatrix);
			// Advance x position by scaled character width
			letterX += (charWidths[c] + spaceX) * scaleX;
		}
	}

	public void draw(String text, float x, float y, float z, float angleDegZ) {
		draw(text, x, y, z, 0, 0, angleDegZ);
	}

	public void draw(String text, float x, float y, float angleDeg) {
		draw(text, x, y, 0, angleDeg);
	}

	public void draw(String text, float x, float y) {
		draw(text, x, y, 0, 0);
	}

	/**
	 * Draw text centered at specified position
	 *
	 * @param text      string to draw
	 * @param x         position to draw text at (bottom left of text)
	 * @param y         position to draw text at (bottom left of text)
	 * @param z         position to draw text at (bottom left of text)
	 * @param angleDegX angle to rotate the text
	 * @param angleDegY angle to rotate the text
	 * @param angleDegZ angle to rotate the text
	 * @return total width of the text that was drawn
	 */
	public float drawC(String text, float x, float y, float z, float angleDegX, float angleDegY, float angleDegZ) {
		float len = getLength(text);
		draw(text, x - (len / 2.0f), y - (getCharHeight() / 2.0f), z, angleDegX, angleDegY, angleDegZ);
		return len;
	}

	public float drawC(String text, float x, float y, float z, float angleDegZ) {
		return drawC(text, x, y, z, 0, 0, angleDegZ);
	}

	public float drawC(String text, float x, float y, float angleDeg) {
		return drawC(text, x, y, 0, angleDeg);
	}

	public float drawC(String text, float x, float y) {
		float len = getLength(text);
		return drawC(text, x - (len / 2.0f), y - (getCharHeight() / 2.0f), 0);

	}

	/**
	 * Draw text centered (x-axis only)
	 *
	 * @param text text to be drawn
	 * @param x    position
	 * @param y    position
	 * @return length
	 */
	public float drawCX(String text, float x, float y) {
		float len = getLength(text);
		draw(text, x - (len / 2.0f), y);
		return len;
	}

	/**
	 * Draw text centered (y-axis only)
	 *
	 * @param text text to be drawn
	 * @param x    position
	 * @param y    position
	 */
	public void drawCY(String text, float x, float y) {
		draw(text, x, y - (getCharHeight() / 2.0f));
	}

	/**
	 * set the scaling to use for the font
	 *
	 * @param scale uniform scale for both x and y axis scaling
	 */
	public void setScale(float scale) {
		scaleX = scaleY = scale;
	}

	/**
	 * set the scaling to use for the font
	 *
	 * @param sx scale for x axis scaling
	 * @param sy scale for y axis scaling
	 */
	public void setScale(float sx, float sy) {
		scaleX = sx;
		scaleY = sy;
	}

	/**
	 * get the current scaling used for the font
	 *
	 * @return x scale
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * get the current scaling used for the font
	 *
	 * @return y scale
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * Get current spacing used for the font
	 *
	 * @return x space currently used for scale
	 */
	public float getSpace() {
		return spaceX;
	}

	/**
	 * Set spacing (unscaled; ie. pixel size) to use for the font
	 *
	 * @param space space for x axis spacing
	 */
	public void setSpace(float space) {
		spaceX = space;
	}

	/**
	 * Return the length of the specified string if rendered using current settings
	 *
	 * @param text string to get length for
	 * @return length of specified string (pixels)
	 */
	public float getLength(String text) {
		float len = 0.0f;
		int strLen = text.length();
		for (int i = 0; i < strLen; i++) {
			int c = (int) text.charAt(i) - CHAR_START;
			len += (charWidths[c] * scaleX);
		}
		len += (strLen > 1 ? ((strLen - 1) * spaceX) * scaleX : 0);
		return len;
	}

	/**
	 * @param chr character to get width for
	 * @return scaled character width
	 */
	public float getCharWidth(char chr) {
		int c = chr - CHAR_START;
		return (charWidths[c] * scaleX);
	}

	/**
	 * @return scaled max character width
	 */
	public float getCharWidthMax() {
		return (charWidthMax * scaleX);
	}

	/**
	 * @return scaled character height
	 */
	public float getCharHeight() {
		return (charHeight * scaleY);
	}

	/**
	 * @return specified (scaled) font metric
	 */
	public float getAscent() {
		return (fontAscent * scaleY);
	}

	/**
	 * @return font descent
	 */
	public float getDescent() {
		return (fontDescent * scaleY);
	}

	/**
	 * @return actual font height
	 */
	public float getHeight() {
		return (fontHeight * scaleY);
	}

}