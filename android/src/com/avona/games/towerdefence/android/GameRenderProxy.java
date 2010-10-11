package com.avona.games.towerdefence.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.PortableMainLoop;

public class GameRenderProxy implements Renderer {
	private PortableMainLoop ml;
	private AndroidDisplay display;

	public GameRenderProxy(PortableMainLoop ml, AndroidDisplay display) {
		this.ml = ml;
		this.display = display;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		ml.performLoop();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		display.onSurfaceChanged(gl, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		display.onSurfaceCreated(gl, config);
	}
}
