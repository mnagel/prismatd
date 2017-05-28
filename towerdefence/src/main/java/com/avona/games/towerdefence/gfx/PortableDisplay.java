package com.avona.games.towerdefence.gfx;

import android.opengl.Matrix;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.gfx.text.DisplayText;
import com.avona.games.towerdefence.input.Layer;

public abstract class PortableDisplay {
	// CTRL-F courtesy: FONTSIZE FONT_SIZE FONT SIZE
	protected final static float FONT_SIZE_RATIO_HEIGHT_FACTOR = 0.5f / 12.0f;
	protected Shader defaultShader;
	private float[] modelMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] modelViewMatrix = new float[16];
	private float[] viewProjectionMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	private float[] mvpMatrix = new float[16];
	private V2 size = new V2();
	private DisplayText displayText;
	private DisplayEventListener eventListener;

	public PortableDisplay(DisplayEventListener eventListener) {
		this.eventListener = eventListener;
	}

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

	public V2 getSize() {
		return size;
	}

	public abstract Texture allocateTexture();

	public abstract Shader allocateShader(String name);

	public abstract void prepareScreen();

	protected void init() {
		defaultShader = allocateShader("default");
		defaultShader.loadShaderProgramFromFile("default");

		Shader textShader = allocateShader("text");
		textShader.loadShaderProgramFromFile("text");

		displayText = allocateDisplayText(textShader);

		eventListener.onNewScreenContext();
		checkGLError("after init");
	}

	protected abstract DisplayText allocateDisplayText(Shader textShader);

	private int getFontSize(int width, int height) {
		float ratio = 800.0f / 480.0f;
		int ratioHeight = (int) ((float) width / ratio);
		ratioHeight = Math.min(height, ratioHeight);
		int fontSize = (int) ((float) ratioHeight * FONT_SIZE_RATIO_HEIGHT_FACTOR + 0.5f);
		return fontSize;
	}

	protected void setSize(int width, int height) {
		size = new V2(width, height);
		displayText.loadFont("Roboto-Regular.ttf", getFontSize(width, height), 2, 2);
		initializeMatrices(width, height);
		eventListener.onReshapeScreen();
	}


	public abstract void drawVertexArray(final VertexArray array);

	public void drawText(final Layer layer, String text, boolean centeredBoth, final V2 location, final RGB color, float alpha) {
		drawText(layer, text, centeredBoth, centeredBoth, location, color, alpha);
	}

	public void drawText(final Layer layer, String text, boolean centeredHorizontal, boolean centeredVertical, final V2 location, final RGB color, float alpha) {
		V2 loc;
		if (layer != null) {
			loc = layer.convertToPhysical(location);
		} else {
			loc = location.clone2();
		}
		V2 textBounds = null;
		if (centeredHorizontal || centeredVertical) {
			textBounds = getTextBounds(text);
		}
		if (centeredHorizontal) {
			loc.x -= textBounds.x / 2;
		}
		if (centeredVertical) {
			loc.y -= textBounds.y / 2;
		}

		displayText.begin(color.R, color.G, color.B, alpha, getViewProjectionMatrix());
		displayText.draw(text, loc.x, loc.y);
		displayText.end();
		checkGLError("after drawText");
	}

	public V2 getTextBounds(final String text) {
		float height = displayText.getHeight();
		float width = displayText.getLength(text);
		return new V2(width, height);
	}

	public abstract void checkGLError(String format, Object... args);
}
