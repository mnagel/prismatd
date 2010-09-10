package com.avona.games.towerdefence;

/**
 * StationaryObject is the base class of all non-moving and moving objects on
 * the screen. It provides a point location and a simple radius for collision
 * detection.
 */
public abstract class StationaryObject {
	public V2 location;
	public float radius;

	/**
	 * Update all state of the object based on the changed in-game time.
	 * 
	 * @param dt
	 *            the time delta since the last invocation of step (1.0 = 1s)
	 */
	public abstract void step(final float dt);

	public boolean collidesWith(final V2 otherLocation,
			final double otherRadius) {
		final double squaredDist = location.dist_sq(otherLocation);
		final double totalRadius = radius + otherRadius;
		final double squaredTotalRadius = totalRadius * totalRadius;
		return (squaredDist < squaredTotalRadius);
	}
}
