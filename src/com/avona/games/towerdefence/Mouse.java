package com.avona.games.towerdefence;

public class Mouse extends StationaryObject {
	public boolean onScreen = true;

	public Mouse() {
		radius = 7;
		location = new V2();
	}

	@Override
	public void step(float dt) {
		// The mouse needs no game state updates.
	}
}
