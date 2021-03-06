package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;

/**
 * This interface contains all input events that might occur. To be as portable
 * as possible, the events should be as input type agnostic as possible and not
 * describe keys and buttons but instead describe the intended purpose.
 */
public interface InputActor {

	void pause();

	void resume();

	void togglePause();

	void mouseBtn1DownAt(V2 location);

	void mouseBtn1UpAt(V2 location);

	void mouseBtn2DownAt(V2 location);

	void mouseBtn2UpAt(V2 location);

	void mouseMovedTo(V2 location);

	void mouseDraggedTo(V2 location);

	void pressedOtherKey(char keyCode);
}