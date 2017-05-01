package com.avona.games.towerdefence.gfx;

import android.opengl.Matrix;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.input.Layer;

public abstract class PortableDisplay {
	private float[] modelMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] modelViewMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	private float[] mvpMatrix = new float[16];

	protected void initializeMatrices(int width, int height) {
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.setIdentityM(viewMatrix, 0);
		Matrix.orthoM(projectionMatrix, 0, 0, width, 0, height, -1, 1);

		updateMvpMatrix();
	}

	private void updateModelViewMatrix() {
		Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
	}

	private void updateMvpMatrix() {
		updateModelViewMatrix();
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
	}

	public void prepareTransformationForLayer(Layer layer) {
		// TODO hook Layer Debug Code here
		float localTransX = layer.offset.x;
		float localTransY = layer.offset.y;

		float localScaleX = layer.region.x / layer.virtualRegion.x;
		float localScaleY = layer.region.y / layer.virtualRegion.y;

		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, localTransX, localTransY, 0);
		Matrix.scaleM(modelMatrix, 0, localScaleX, localScaleY, 1);
		updateModelViewMatrix();
	}

	protected float[] getProjectionMatrix() {
		return projectionMatrix;
	}

	protected float[] getModelViewMatrix() {
		updateModelViewMatrix();
		return modelViewMatrix;
	}

	protected float[] getMvpMatrix() {
		updateMvpMatrix();
		return mvpMatrix;
	}

	public abstract V2 getSize();

	public abstract Texture allocateTexture();

	public abstract Shader allocateShader(String name);

	public abstract void prepareScreen();

	public abstract void drawVertexArray(final VertexArray array);

	public abstract void drawText(final Layer layer, String text, boolean centered, final V2 location, final RGB color, float alpha);

	public abstract V2 getTextBounds(final String text);

	public abstract void checkGLError(String trace);
}
