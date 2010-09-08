package com.avona.games.towerdefence;

import javax.vecmath.Point2d;

/**
 * StationaryObject is the base class of all non-moving and moving objects on
 * the screen. It provides a point location and a simple radius for collision
 * detection.
 */
public abstract class StationaryObject {
	public Point2d location;
	public double radius;

	/**
	 * Update all state of the object based on the changed in-game time.
	 * 
	 * @param dt
	 *            the time delta since the last invocation of step (1.0 = 1s)
	 */
	public abstract void step(final double dt);
}
