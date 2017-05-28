package com.avona.games.towerdefence.gfx.text;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/GLText.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import android.opengl.Matrix;
import com.avona.games.towerdefence.gfx.GlWrapper;
import com.avona.games.towerdefence.gfx.PortableDisplay;
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
public abstract class DisplayText {
	protected static final int CHAR_START = 32; // First character (ASCII)
	protected static final int CHAR_END = 126; // Last character (ASCII)
	protected static final int CHAR_CNT = (((CHAR_END - CHAR_START) + 1) + 1); // Character count (including character to use for unknown)
	protected static final int CHAR_NONE = 32; // Character to use for unknown (ASCII)
	protected static final int CHAR_UNKNOWN = (CHAR_CNT - 1); // Index of the unknown character
	protected static final int FONT_SIZE_MIN = 6; // Minimum font size (pixels)
	protected static final int FONT_SIZE_MAX = 180; // Maximum font size (pixels)
	protected static final int CHAR_BATCH_SIZE = 24; // Number of characters to render per batch, must be the same as the size of u_mvpMatrices in shader
	protected final float[] charWidths; // Width of each character (pixels)
	protected final TextureRegion[] charRgn; // Region of each character (texture coordinates)
	protected final int mShaderProgramHandle;
	protected final int mColorHandle;
	protected final int mTextureUniformHandle;
	protected final GlWrapper glWrapper;
	protected final PortableDisplay display;
	protected final Shader shader;
	protected SpriteBatch batch; // Batch renderer
	protected int fontPadX; // Horizontal font padding (pixels)
	protected int fontPadY; // Vertical font padding (pixels)
	protected float fontHeight; // Font height (pixels)
	protected float fontAscent; // Font ascent (above baseline; pixels)
	protected float fontDescent; // Font descent (below baseline; pixels)
	protected float charWidthMax; // Character width (pixels)
	protected float charHeight; // Character height (pixels)
	protected int cellWidth;
	protected int cellHeight; // Character cell width / height
	protected int textureId = -1;
	private float scaleX;
	private float scaleY; // Font scale
	private float spaceX; // Additional spacing (unscaled)

	public DisplayText(GlWrapper glWrapper, PortableDisplay display, Shader shader) {
		this.glWrapper = glWrapper;
		this.display = display;
		this.shader = shader;
		this.mShaderProgramHandle = shader.getProgram();
		this.mColorHandle = shader.getUniformLocation("u_color");
		this.mTextureUniformHandle = shader.getUniformLocation("u_texture");

		charWidths = new float[CHAR_CNT];
		charRgn = new TextureRegion[CHAR_CNT];

		fontPadX = 0;
		fontPadY = 0;

		fontHeight = 0.0f;
		fontAscent = 0.0f;
		fontDescent = 0.0f;

		charWidthMax = 0;
		charHeight = 0;

		cellWidth = 0;
		cellHeight = 0;

		scaleX = 1.0f;
		scaleY = 1.0f;
		spaceX = 0.0f;

		batch = new SpriteBatch(glWrapper, CHAR_BATCH_SIZE, shader);
	}

	/**
	 * Load the specified font file, create a texture for the defined character range, and setup all required values used to render with it.
	 *
	 * @param file filename of the font (.ttf, .otf) to use in assets
	 * @param size requested pixel size of font (height)
	 * @return success
	 */
	protected abstract boolean loadFont(String file, int size);

	/**
	 * Load the specified font file, create a texture for the defined character range, and setup all required values used to render with it.
	 *
	 * @param file filename of the font (.ttf, .otf) to use in assets
	 * @param size requested pixel size of font (height)
	 * @param padX extra padding per character to prevent overlapping characters.
	 * @param padY extra padding per character to prevent overlapping characters.
	 * @return success
	 */
	public boolean loadFont(String file, int size, int padX, int padY) {
		fontPadX = padX;
		fontPadY = padY;

		return loadFont(file, size);
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
		preDraw(red, green, blue, alpha);
		batch.beginBatch(vpMatrix);
	}

	public void end() {
		batch.endBatch();
		postDraw();
	}

	private void postDraw() {
	}

	private void preDraw(float red, float green, float blue, float alpha) {
		glWrapper.glUseProgram(mShaderProgramHandle);
		float[] color = {red, green, blue, alpha};
		glWrapper.glUniform4fv(mColorHandle, 1, color, 0);
		glWrapper.glActiveTexture(glWrapper.GL_TEXTURE0);
		glWrapper.glBindTexture(glWrapper.GL_TEXTURE_2D, textureId);
		glWrapper.glUniform1i(mTextureUniformHandle, 0);
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

		// TODO: repair text rotation (Matrix implementation copied from android results in empty matrix after call to Matrix.rotateM)
//		Matrix.rotateM(modelMatrix, 0, angleDegZ, 0, 0, 1);
//		Matrix.rotateM(modelMatrix, 0, angleDegX, 1, 0, 0);
//		Matrix.rotateM(modelMatrix, 0, angleDegY, 0, 1, 0);

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