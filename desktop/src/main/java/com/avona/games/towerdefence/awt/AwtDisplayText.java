package com.avona.games.towerdefence.awt;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureHelper.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

import com.avona.games.towerdefence.gfx.GlWrapper;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.text.DisplayText;
import com.avona.games.towerdefence.gfx.text.TextureRegion;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("AccessStaticViaInstance")
public class AwtDisplayText extends DisplayText {
	public AwtDisplayText(GlWrapper glWrapper, AwtDisplay display, Shader shader) {
		super(glWrapper, display, shader);
	}

	@Override
	protected boolean loadFont(String file, int size) {
		// TODO it must be possible to implement the next 15 lines in a simpler way
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(file);
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			throw new RuntimeException(e); // TODO
		}
		font = font.deriveFont(Font.PLAIN, size);
		FontMetrics fm;
		{
			Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
			g2d.setFont(font);
			fm = g2d.getFontMetrics();
		}

		// get font metrics
		fontHeight = fm.getHeight();
		fontAscent = (float) Math.ceil(Math.abs(fm.getAscent()));
		fontDescent = (float) Math.ceil(Math.abs(fm.getDescent()));

		// determine the width of each character and max width (including unknown character)
		charWidthMax = charHeight = 0;
		int cnt = 0;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			charWidths[cnt] = fm.charWidth(c);
			if (charWidths[cnt] > charWidthMax) {
				charWidthMax = charWidths[cnt];
			}
			cnt++;
		}
		charWidths[cnt] = fm.charWidth(CHAR_NONE);
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
		BufferedImage image = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		graphics.setFont(font);
		graphics.setColor(new Color(0xffffffff));
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		char[] s = new char[2];

		// render each of the characters to the canvas (ie. build the font map)
		float x = fontPadX;
		float y = (cellHeight - 1) - fontDescent - fontPadY;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			s[0] = c;
			graphics.drawString(new String(s), x, y);
			x += cellWidth;
			if ((x + cellWidth - fontPadX) > textureSize) {
				x = fontPadX;
				y += cellHeight;
			}
		}
		s[0] = CHAR_NONE;
		graphics.drawString(new String(s), x, y);

		// save the bitmap in a texture
		AwtTexture texture = (AwtTexture) display.allocateTexture();
		texture.loadImage(image);
		textureId = texture.textureId;

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
}
