package com.avona.games.towerdefence;


public abstract class MovingObject extends StationaryObject {
	public VelocityVector velocity = new VelocityVector();
	public World world;
}
