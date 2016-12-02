package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.V2;

/**
 * This interface contains all input events that might occur. To be as portable
 * as possible, the events should be as input type agnostic as possible and not
 * describe keys and buttons but instead describe the intended purpose.
 */
public interface InputActor {

	public void pause();

	public void resume();

	public void togglePause();

	public void mouseBtn1DownAt(V2 location);

	public void mouseBtn1UpAt(V2 location);

	public void mouseBtn2DownAt(V2 location);

	public void mouseBtn2UpAt(V2 location);

	public void mouseEntered();

	public void mouseExited();

	public void mouseMovedTo(V2 location);

	public void mouseDraggedTo(V2 location);

	public void pressedMenuKey();

	/**
	 * Used as debugging key for debugging purposes.
	 * @param keyCode
	 */
	public void pressedOtherKey(char keyCode);
}