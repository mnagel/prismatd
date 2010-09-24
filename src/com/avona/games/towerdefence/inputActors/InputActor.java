package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.V2;

public interface InputActor {

	public void pressedEscapeKey();

	public void onPause();

	public void onResume();

	public void pressedSpaceKey();

	public void mouseBtn1DownAt(V2 location);

	public void mouseBtn1UpAt(V2 location);

	public void mouseBtn2DownAt(V2 location);

	public void mouseBtn2UpAt(V2 location);

	public void mouseEntered();

	public void mouseExited();

	public void mouseMovedTo(V2 location);

	public void mouseDraggedTo(V2 location);

}