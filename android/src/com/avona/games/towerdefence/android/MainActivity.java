package com.avona.games.towerdefence.android;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.World;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

class ClearGLSurfaceView extends GLSurfaceView { // FIXME -- i want to become something like InputMangler!
    public ClearGLSurfaceView(Context context) {
        super(context);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {

                if (event.getAction() ==  MotionEvent.ACTION_DOWN ) {
                	Point2d p = new Point2d(event.getX(), 800 - event.getY());
                	hack.game.addEnemyAt(p);
                }
                
                if (event.getAction() ==  MotionEvent.ACTION_UP ) {
                	Point2d p = new Point2d(event.getX(), 800 - event.getY());
                	hack.game.addTowerAt(p);
                }
            	
                // FIXME -- Placement is OFF!

            }});
            return true;
        }
    
    public TDRenderer hack; // FIXME -- i want to be refactored
}

public class MainActivity extends Activity {
	private ClearGLSurfaceView glSurfaceView;

	/** Called when the activity is first created. */
	@Override        
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new ClearGLSurfaceView(this);
		TDRenderer r = new TDRenderer();
		glSurfaceView.setRenderer(r);
		glSurfaceView.hack = r;
		setContentView(glSurfaceView);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
