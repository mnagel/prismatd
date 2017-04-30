package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.engine.PortableMainLoop;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

class AndroidMainLoop extends PortableMainLoop {
	private static final long serialVersionUID = 1L;
	private final Activity activity;

	GLSurfaceView surfaceView;

	AndroidMainLoop(Activity activity, Vibrator vibrator) {
		super();
		this.activity = activity;

		game = new Game(eventDistributor);
		initWithGame();

		ResourceResolverRegistry.setInstance(new AndroidResourceResolver(activity.getResources()));

		final AndroidDisplay display = new AndroidDisplay(activity, displayEventListener);
		ge = new PortableGraphicsEngine(display, game, mouse, layerHerder, this);
		displayEventListener.add(ge);

		eventDistributor.listeners.add(new AndroidEventListener(vibrator));

		surfaceView = new InputForwardingGLSurfaceView(activity, rootInputActor, display);
		surfaceView.setEGLContextClientVersion(2);
		surfaceView.setRenderer(new GameRenderProxy(this, display));
	}

	@Override
	public void exit() {
		activity.finish();
	}
}
