package com.avona.games.towerdefence.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.PortableMainLoop;

public class GameRenderProxy implements Renderer {
	private PortableMainLoop ml;
	private GraphicsEngine ge;

	public GameRenderProxy(PortableMainLoop ml, GraphicsEngine ge) {
		this.ml = ml;
		this.ge = ge;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		ml.performLoop();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		ge.onSurfaceChanged(gl, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		ge.onSurfaceCreated(gl, config);
	}
}
