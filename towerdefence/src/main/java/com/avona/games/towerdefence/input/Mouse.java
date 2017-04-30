package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;

public class Mouse {
	public V2 physicalLocation;
	public float radius;

	public boolean onScreen = false;

	public Mouse() {
		radius = 7;
		physicalLocation = new V2();
	}
}
