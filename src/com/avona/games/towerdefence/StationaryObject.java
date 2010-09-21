package com.avona.games.towerdefence;

/**
 * StationaryObject is the base class of all non-moving and moving objects on
 * the screen. It provides a point location and a simple radius for collision
 * detection.
 */
public abstract class StationaryObject {
	public V2 location;
	public float radius;

	protected StationaryObject() {
	}

	protected StationaryObject(final StationaryObject o) {
		if (o.location != null)
			location = new V2(o.location);
		radius = o.radius;
	}

	/**
	 * Update all state of the object based on the changed in-game time.
	 * 
	 * @param dt
	 *            the time delta since the last invocation of step (1.0 = 1s)
	 */
	public abstract void step(final float dt);

	public boolean collidesWith(final V2 otherLocation, final float otherRadius) {
		return Collision.circleCollidesWithCircle(location, radius,
				otherLocation, otherRadius);
	}
}
