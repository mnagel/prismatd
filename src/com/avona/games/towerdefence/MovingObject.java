package com.avona.games.towerdefence;

public abstract class MovingObject extends LocationObject {
	private static final long serialVersionUID = 1L;

	public V2 velocity = new V2(1.0f, 1.0f);
	public World world;
}
