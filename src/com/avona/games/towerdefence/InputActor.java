package com.avona.games.towerdefence;

public interface InputActor {

	public abstract void pressedEscapeKey();

	public abstract void onPause();

	public abstract void onResume();

	public abstract void pressedSpaceKey();

	public abstract void pressedMouseBtn1At(V2 location);

	public abstract void pressedMouseBtn2At(V2 location);

	public abstract void mouseEntered();

	public abstract void mouseExited();

	public abstract void mouseMovedTo(V2 location);

	public abstract void mouseDraggedTo(V2 location);

}