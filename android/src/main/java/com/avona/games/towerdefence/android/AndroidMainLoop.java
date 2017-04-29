package com.avona.games.towerdefence.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

class AndroidMainLoop extends PortableMainLoop {
	private static final long serialVersionUID = 1L;

	GLSurfaceView surfaceView;

	// TODO startMission is evil
	AndroidMainLoop(Context context, Vibrator vibrator, int startMission) {
		super();

		// TODO startMission is evil
		game = new Game(eventListener, startMission);
		initWithGame();

		ResourceResolverRegistry.setInstance(new AndroidResourceResolver(context.getResources()));

		final AndroidDisplay display = new AndroidDisplay(context, displayEventListener);
		ge = new PortableGraphicsEngine(display, game, mouse, layerHerder, this);
		displayEventListener.add(ge);

		setupInputActors();

		eventListener.listeners.add(new AndroidEventListener(vibrator));

		surfaceView = new InputForwardingGLSurfaceView(context, inputActor, display);
		surfaceView.setEGLContextClientVersion(2);
		surfaceView.setRenderer(new GameRenderProxy(this, display));
	}

	@Override
	public void exit() {
		// TODO Determine how an application properly exits in Android.
	}
}
