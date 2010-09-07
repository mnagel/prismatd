package com.avona.games.towerdefence;

import javax.vecmath.Point2d;


/**
 * StationaryObject is the base class of all non-moving and moving
 * objects on the screen.  It provides a point location and a simple
 * radius for collision detection.  
 */
public abstract class StationaryObject {
	public Point2d location;
	public double radius;
}
