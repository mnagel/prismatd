package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import com.avona.games.towerdefence.InputActor;
import com.avona.games.towerdefence.PortableMainLoop;

public class MainLoop extends PortableMainLoop {
	public Activity activity;
	public GLSurfaceView surfaceView;

	public MainLoop(Activity activity) {
		this.activity = activity;
		GraphicsEngine graphicsEngine = new GraphicsEngine(game);
		ge = graphicsEngine;

		inputActor = new InputActor(this, game);
		GameRenderProxy r = new GameRenderProxy(this, graphicsEngine);
		inputActor = new InputActor(this, game);
		
		surfaceView = new InputForwardingGLSurfaceView(activity, inputActor);
		surfaceView.setRenderer(r);
		activity.setContentView(surfaceView);
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}
}
