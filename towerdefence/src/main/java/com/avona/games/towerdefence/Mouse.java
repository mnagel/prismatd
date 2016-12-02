package com.avona.games.towerdefence;

public class Mouse {
	public V2 location;
	public float radius;

	public boolean onScreen = false;

	public Mouse() {
		radius = 7;
		location = new V2();
	}
}
