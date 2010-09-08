package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class World {
	public VelocityVector getDirection(Point2d location) {
		return new VelocityVector(new Vector2d(-1.0, 0.0), 0.03);
	}

	public Point2d getInitialLocation() {
		return new Point2d(0.0, 0.1);
	}
}
