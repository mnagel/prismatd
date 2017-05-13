package com.avona.games.towerdefence.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.gfx.PortableDisplay;
import com.avona.games.towerdefence.input.InputActor;
import com.avona.games.towerdefence.util.Util;

class InputForwardingGLSurfaceView extends GLSurfaceView {
	private InputActor inputActor;
	private PortableDisplay display;

	InputForwardingGLSurfaceView(Context context, InputActor inputActor, PortableDisplay display) {
		super(context);
		this.inputActor = inputActor;
		this.display = display;
	}

	private V2 eventLocation(final MotionEvent event) {
		return new V2(event.getX(), display.getSize().y - event.getY());
	}

	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				queueEvent(new Runnable() {
					@Override
					public void run() {
						inputActor.mouseBtn1DownAt(eventLocation(event));
					}
				});
				break;
			case MotionEvent.ACTION_UP:
				queueEvent(new Runnable() {
					@Override
					public void run() {
						inputActor.mouseBtn1UpAt(eventLocation(event));
					}
				});
				break;
			case MotionEvent.ACTION_MOVE:
				queueEvent(new Runnable() {
					@Override
					public void run() {
						inputActor.mouseMovedTo(eventLocation(event));
					}
				});
				break;
			default:
				Util.log("Unexpected TouchEvent...");
		}
		return true;
	}
}