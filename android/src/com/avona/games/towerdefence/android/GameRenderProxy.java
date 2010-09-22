package com.avona.games.towerdefence.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.PortableMainLoop;

import com.example.google.SpriteTextRenderer;

public class GameRenderProxy extends SpriteTextRenderer implements Renderer {
	public PortableMainLoop ml;
	public GraphicsEngine ge;

	public GameRenderProxy(Context activity, PortableMainLoop ml, GraphicsEngine ge) {
		super(activity);
		
		this.ml = ml;
		this.ge = ge;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		ml.performLoop();
		
		onDrawFrame2( gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		super.onSurfaceChanged( gl,  width,  height);
		
		ge.onSurfaceChanged(gl, width, height);
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		super.onSurfaceCreated( gl,  config);
		ge.onSurfaceCreated(gl, config);
		
		
	}
}
