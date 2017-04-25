package com.avona.games.towerdefence;

import java.io.Serializable;

/**
 * The base class of all non-moving and moving objects on the screen. It
 * provides a point location and a simple radius for collision detection.
 */
public abstract class LocationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public V2 location;
	public float radius;

	// used in gfx engine and elsewhere
	public float seed = (float)Math.random();

	protected LocationObject() {
	}

	protected LocationObject(final LocationObject o) {
		if (o.location != null)
			location = o.location.clone();
		radius = o.radius;
	}

	/**
	 * Update all state of the object based on the changed in-game time.
	 *
	 * @param dt the time delta since the last invocation of step (1.0 = 1s)
	 */
	public abstract void step(final float dt);

	public boolean collidesWith(final V2 otherLocation, final float otherRadius) {
		return Collision.circleCollidesWithCircle(location, radius, otherLocation, otherRadius);
	}
}
