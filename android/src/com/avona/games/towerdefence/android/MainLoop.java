package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import com.avona.games.towerdefence.InputActor;
import com.avona.games.towerdefence.PortableMainLoop;

public class MainLoop extends PortableMainLoop {
	public Activity activity;
	public GLSurfaceView surfaceView;

	public MainLoop(Activity activity) {
		super();

		this.activity = activity;

		final GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder);
		ge = graphicsEngine;

		inputActor = new InputActor(this, game, mouse, layerHerder);

		surfaceView = new InputForwardingGLSurfaceView(activity, inputActor, ge);
		final GameRenderProxy r = new GameRenderProxy(this, graphicsEngine);
		surfaceView.setRenderer(r);
		activity.setContentView(surfaceView);
	}

	@Override
	public void exit() {
		// TODO Determine how an application properly exits in Android.
	}
}
