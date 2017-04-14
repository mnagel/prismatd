package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;

/**
 * This class provides all basic drawing primitives.
 */
public interface Display {

	public V2 getSize();

	public Texture allocateTexture();

	public void prepareTransformationForLayer(Layer layer);

	public void resetTransformation();

	public void prepareScreen();

	public void drawVertexArray(final VertexArray array);

	public void drawText(final Layer layer, String text, boolean centered, final V2 location, final RGB color, float alpha);

	public V2 getTextBounds(final String text);

	public int userSelectsAString(String title, String message, String[] strings);
}
