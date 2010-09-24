package com.avona.games.towerdefence.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.avona.games.towerdefence.PortableGraphicsEngine;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.inputActors.InputActor;

class InputForwardingGLSurfaceView extends GLSurfaceView {
	private InputActor inputActor;
	private PortableGraphicsEngine ge;
	private Vibrator vibrator;

	public InputForwardingGLSurfaceView(Context context, InputActor inputActor,
			PortableGraphicsEngine ge, Vibrator vibrator) {
		super(context);
		this.inputActor = inputActor;
		this.ge = ge;
		this.vibrator = vibrator;
	}

	private V2 eventLocation(final MotionEvent event) {
		return new V2(event.getX(), ge.size.y - event.getY());
	}

	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					inputActor.pressedMouseBtn1At(eventLocation(event));
					vibrator.vibrate(25);
				}
			}
		});
		return true;
	}
}