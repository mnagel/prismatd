package com.avona.games.towerdefence.android;

import javax.microedition.khronos.opengles.GL;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import com.avona.games.towerdefence.PortableMainLoop;
import com.example.google.MatrixTrackingGL;

public class MainLoop extends PortableMainLoop {
	public Activity activity;
	public GLSurfaceView surfaceView;

	public MainLoop(Activity activity) {
		super();

		this.activity = activity;

		final GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder, graphicsTime);
		ge = graphicsEngine;

		setupInputActors();

		surfaceView = new InputForwardingGLSurfaceView(activity, inputActor, ge);
		
		surfaceView.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }});
		
		
		final GameRenderProxy r = new GameRenderProxy(activity, this, graphicsEngine);
		surfaceView.setRenderer(r);
		activity.setContentView(surfaceView);
	}

	@Override
	public void exit() {
		// TODO Determine how an application properly exits in Android.
	}
}
