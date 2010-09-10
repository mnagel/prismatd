package com.avona.games.towerdefence;

public class Tower extends StationaryObject {
	public double range_sq = 200 * 200;
	protected RechargeTimer timer = new RechargeTimer(4.0);
	public boolean showRange = false;

	public Tower(V2 location) {
		this.location = location;
	}

	public boolean inRange(Enemy e) {
		return location.dist_sq(e.location) < range_sq;
	}

	public Particle shootTowards(Enemy e, final float dt) {
		if (timer.isReady()) {
			timer.rearm();
			return new Particle(location, e);
		} else {
			return null;
		}
	}

	@Override
	public void step(float dt) {
		timer.step(dt);
	}
}
