package com.avona.games.towerdefence;

public abstract class MovingObject extends LocationObject {
	public V2 velocity = new V2(1.0f, 1.0f);
	public World world;
}
