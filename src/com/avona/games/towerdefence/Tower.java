package com.avona.games.towerdefence;

import javax.vecmath.Point2d;


public class Tower extends StationaryObject {
	protected double range = 0.8;
	protected RechargeTimer timer = new RechargeTimer(4.0);

	public Tower(Point2d location) {
		this.location = location;
	}
	
	public boolean inRange(Enemy e) {
		return location.distance(e.location) < range;
	}

	public Particle shootTowards(Enemy e, final double dt) {
		if(timer.isReady()) {
			timer.rearm();
			return new Particle(location, e);
		} else {
			return null;
		}
	}

	@Override
	public void step(double dt) {
		timer.step(dt);
	}
}
