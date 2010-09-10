package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private MainLoop ml;

	/** Called when the activity is first created. */
	@Override        
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ml = new MainLoop(this);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        ml.inputActor.onPause();
        ml.surfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ml.inputActor.onResume();
        ml.surfaceView.onResume();
    }
}
