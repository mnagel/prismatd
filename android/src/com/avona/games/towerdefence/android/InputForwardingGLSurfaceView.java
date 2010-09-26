package com.avona.games.towerdefence.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.inputActors.InputActor;

class InputForwardingGLSurfaceView extends GLSurfaceView {
	private InputActor inputActor;
	private PortableGraphicsEngine ge;

	public InputForwardingGLSurfaceView(Context context, InputActor inputActor,
			PortableGraphicsEngine ge) {
		super(context);
		this.inputActor = inputActor;
		this.ge = ge;
	}

	private V2 eventLocation(final MotionEvent event) {
		return new V2(event.getX(), ge.size.y - event.getY());
	}

	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					inputActor.mouseBtn1DownAt(eventLocation(event));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					inputActor.mouseBtn1UpAt(eventLocation(event));
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					inputActor.mouseDraggedTo(eventLocation(event));
				}
			}
		});
		return true;
	}
}