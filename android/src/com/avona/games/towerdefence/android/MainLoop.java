package com.avona.games.towerdefence.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;

import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

public class MainLoop extends PortableMainLoop {
	private static final long serialVersionUID = 1L;

	public GLSurfaceView surfaceView;

	public MainLoop(Context context, Vibrator vibrator) {
		super();

		ResourceResolverRegistry.setInstance(new AndroidResourceResolver(
				context.getResources()));

		final GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder);
		ge = graphicsEngine;

		setupInputActors();

		eventListener.listeners.add(new AndroidEventListener(vibrator));

		surfaceView = new InputForwardingGLSurfaceView(context, inputActor, ge);
		surfaceView.setRenderer(new GameRenderProxy(this, graphicsEngine));
	}

	@Override
	public void exit() {
		// TODO Determine how an application properly exits in Android.
	}
}
