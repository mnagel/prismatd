package com.avona.games.towerdefence;

/**
 * The vector describes both velocity (speed) and direction (vector). 1.0 is the
 * full movement of the vector in a second.
 */
public class VelocityVector { // TODO remove this class and merge content into
	// V2
	/**
	 * vector is always normalized. Otherwise translate will yield wrong values.
	 * You can either fill the variable by calling its normalize method with the
	 * new vector or by calling setVector(Vector2d) which does this for you.
	 */
	public V2 direction = new V2();
	public float speed = 0.0f;

	public VelocityVector() {
	}

	public VelocityVector(V2 vector, float speed) {
		setVector(vector);
		this.speed = speed;
	}

	public void translate(V2 p, float dt) {
		p.add(new V2(direction.x * speed * dt, direction.y * speed * dt));
	}

	public void setVector(V2 vector) {
		// this.direction.normalize(vector);
		direction = vector;
		direction.normalize();
	}

	public void fromto(V2 location, V2 target) {
		V2 from = new V2(location.x, location.y);
		V2 to = new V2(target.x, target.y);
		to.sub(from);
		setVector(to);
	}

	public String toString() {
		return String.format("VelocityVector(Vector=<%f, %f>, Speed=%f)",
				direction.x, direction.y, speed);
	}
}
