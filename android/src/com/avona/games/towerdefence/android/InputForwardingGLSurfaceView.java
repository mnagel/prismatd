package com.avona.games.towerdefence.android;

import javax.vecmath.Point2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.avona.games.towerdefence.InputActor;

class InputForwardingGLSurfaceView extends GLSurfaceView {
	private InputActor inputActor;

    public InputForwardingGLSurfaceView(Context context, InputActor inputActor) {
        super(context);
        this.inputActor = inputActor;
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
        		if (event.getAction() == MotionEvent.ACTION_DOWN) {
        			Point2d p = new Point2d(event.getX(), 800 - event.getY());
        			inputActor.pressedMouseBtn1At(p);
        		}

        		if (event.getAction() == MotionEvent.ACTION_UP) {
        			Point2d p = new Point2d(event.getX(), 800 - event.getY());
        			inputActor.pressedMouseBtn2At(p);
        		}
            }});
            return true;
        }
}