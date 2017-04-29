package com.avona.games.towerdefence.android;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.avona.games.towerdefence.PortableMainLoop;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.concurrent.ConcurrentLinkedQueue;


public class GameRenderProxy implements Renderer {
	private static final String TAG = "GameRenderProxy";
	// TODO find a good place for this
	public static ConcurrentLinkedQueue<Runnable> msgs = new ConcurrentLinkedQueue<>();
	private PortableMainLoop ml;
	private AndroidDisplay display;

	public GameRenderProxy(PortableMainLoop ml, AndroidDisplay display) {
		this.ml = ml;
		this.display = display;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		Runnable r = msgs.poll();
		if (r != null) {
			Log.v(TAG, "Processing message in GameRenderProxy");
			r.run();
		}
		ml.performIteration();
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
