package com.avona.games.towerdefence.engine;

import com.avona.games.towerdefence.core.V2;

public abstract class MovingObject extends LocationObject {
	private static final long serialVersionUID = 1L;

	private V2 velocity = new V2(1.0f, 1.0f);

	public MovingObject(V2 location, float radius) {
		super(location, radius);
	}

	public MovingObject(final MovingObject other) {
		super(other);
		if (other.getVelocity() != null)
			setVelocity(other.getVelocity().clone2());
	}

	public V2 getVelocity() {
		return velocity;
	}

	public void setVelocity(final V2 velocity) {
		this.velocity = velocity;
	}
}
