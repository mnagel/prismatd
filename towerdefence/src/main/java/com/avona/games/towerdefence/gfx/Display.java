package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.input.Layer;

/**
 * This class provides all basic drawing primitives.
 */
public interface Display {

	V2 getSize();

	Texture allocateTexture();

	Shader allocateShader(String name);

	void prepareTransformationForLayer(Layer layer);

	void prepareScreen();

	void drawVertexArray(final VertexArray array);

	void drawText(final Layer layer, String text, boolean centered, final V2 location, final RGB color, float alpha);

	V2 getTextBounds(final String text);

	void checkGLError(String trace);
}
