package com.avona.games.towerdefence;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;

/**
 * The vector describes both velocity (speed) and direction (vector). 1.0 is the
 * full movement of the vector in a second.
 */
public class VelocityVector {
	/**
	 * vector is always normalized. Otherwise translate will yield wrong values.
	 * You can either fill the variable by calling its normalize method with the
	 * new vector or by calling setVector(Vector2d) which does this for you.
	 */
	public Vector2d vector = new Vector2d();
	public double speed = 0.0;

	public VelocityVector() {
	}

	public VelocityVector(Vector2d vector, double speed) {
		setVector(vector);
		this.speed = speed;
	}

	public void translate(Point2d p, double dt) {
		p.add(new Point2d(vector.x * speed * dt, vector.y * speed * dt));
	}

	public void setVector(Vector2d vector) {
		this.vector.normalize(vector);
	}

	public String toString() {
		return String.format("VelocityVector(Vector=<%f, %f>, Speed=%f)",
				vector.x, vector.y, speed);
	}
}
