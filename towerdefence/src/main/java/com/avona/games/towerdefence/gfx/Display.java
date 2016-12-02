package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.Layer;
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

	public void drawText(final String text, final double x,
			final double y, final float colR, final float colG,
			final float colB, final float colA);

	public V2 getTextBounds(final String text);

	public int userSelectsAString(String title, String message, String[] strings);
}
