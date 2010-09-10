package com.avona.games.towerdefence.android;

import javax.vecmath.Point2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.avona.games.towerdefence.InputActor;
import com.avona.games.towerdefence.PortableGraphicsEngine;

class InputForwardingGLSurfaceView extends GLSurfaceView {
	private InputActor inputActor;
	private PortableGraphicsEngine ge;

	public InputForwardingGLSurfaceView(Context context, InputActor inputActor,
			PortableGraphicsEngine ge) {
		super(context);
		this.inputActor = inputActor;
		this.ge = ge;
	}

	private Point2d eventLocation(final MotionEvent event) {
		return new Point2d(event.getX(), ge.size.y - event.getY());
	}

	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					inputActor.pressedMouseBtn1At(eventLocation(event));
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					inputActor.pressedMouseBtn2At(eventLocation(event));
				}
			}
		});
		return true;
	}
}