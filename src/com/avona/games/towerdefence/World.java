package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class World {
	public Point2d initLocation = new Point2d();
	
	public VelocityVector getDirection(Point2d location) {
		return new VelocityVector(new Vector2d(-1.0, 0.0), 0.03);
	}

	public Point2d getInitialLocation() {
		return initLocation;
	}
}
