package com.avona.games.towerdefence.gfx;

import android.opengl.Matrix;
import com.avona.games.towerdefence.input.Layer;

import java.util.Stack;

public abstract class PortableDisplay implements Display {
	private float[] modelMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] modelViewMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	private float[] mvpMatrix = new float[16];
	private Stack<float[]> modelMatrixStack = new Stack<>();

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

	@Override
	public void prepareTransformationForLayer(Layer layer) {
		modelMatrixStack.push(modelMatrix.clone());
		Matrix.translateM(modelMatrix, 0, layer.offset.x, layer.offset.y, 0);
		Matrix.scaleM(modelMatrix, 0, layer.region.x / layer.virtualRegion.x, layer.region.y / layer.virtualRegion.y, 1);
		updateModelViewMatrix();
	}

	@Override
	public void resetTransformation() {
		modelMatrix = modelMatrixStack.pop();
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
}
