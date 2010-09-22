package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.avona.games.towerdefence.PortableMainLoop;

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

		surfaceView = new InputForwardingGLSurfaceView(activity, inputActor, ge, (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE));
		final GameRenderProxy r = new GameRenderProxy(this, graphicsEngine);
		surfaceView.setRenderer(r);
		
		FrameLayout v = (FrameLayout)activity.findViewById(R.id.gl_frame);
		v.addView(surfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		v.setVisibility(View.VISIBLE);
	}

	@Override
	public void exit() {
		// TODO Determine how an application properly exits in Android.
	}
}
