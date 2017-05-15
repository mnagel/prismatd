package com.avona.games.towerdefence.gfx;

import android.opengl.Matrix;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.input.Layer;

public abstract class PortableDisplay {
	// CTRL-F courtesy: FONTSIZE FONT_SIZE FONT SIZE
	protected final static float FONT_SIZE_RATIO_HEIGHT_FACTOR = 0.5f / 12.0f;
	private float[] modelMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] modelViewMatrix = new float[16];
	private float[] viewProjectionMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	private float[] mvpMatrix = new float[16];

	protected void initializeMatrices(int width, int height) {
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.setIdentityM(viewMatrix, 0);
		Matrix.orthoM(projectionMatrix, 0, 0, width, 0, height, -1, 1);

		refreshMatrices();
	}

	private void refreshMatrices() {
		Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);

		Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
	}

	public void prepareTransformationForLayer(Layer layer) {
		float localTransX = layer.offset.x;
		float localTransY = layer.offset.y;

		float localScaleX = layer.region.x / layer.virtualRegion.x;
		float localScaleY = layer.region.y / layer.virtualRegion.y;

		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, localTransX, localTransY, 0);
		Matrix.scaleM(modelMatrix, 0, localScaleX, localScaleY, 1);

		refreshMatrices();
	}

	protected float[] getViewProjectionMatrix() {
		return viewProjectionMatrix;
	}

	protected float[] getMvpMatrix() {
		return mvpMatrix;
	}

	public abstract V2 getSize();

	public abstract Texture allocateTexture();

	public abstract Shader allocateShader(String name);

	public abstract void prepareScreen();

	public abstract void drawVertexArray(final VertexArray array);

	public void drawText(final Layer layer, String text, boolean centeredBoth, final V2 location, final RGB color, float alpha) {
		drawText(layer, text, centeredBoth, centeredBoth, location, color, alpha);
	}

	public abstract void drawText(final Layer layer, String text, boolean centeredHorizontal, boolean centeredVertical, final V2 location, final RGB color, float alpha);

	public abstract V2 getTextBounds(final String text);

	public abstract void checkGLError(String format, Object... args);
}
