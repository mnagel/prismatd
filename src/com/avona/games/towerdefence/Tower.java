package com.avona.games.towerdefence;

public class Tower extends StationaryObject {
	public float range = 200;
	// FIXME range_sq and range should probably be merged or forced to be in
	// sync some other way.
	public float range_sq = range * range;
	protected RechargeTimer timer = new RechargeTimer(0.3);
	public boolean showRange = false;

	public Tower(V2 location) {
		this.location = location;
	}

	public boolean inRange(Enemy e) {
		return location.dist_sq(e.location) < range_sq;
	}

	public Particle shootTowards(Enemy e) {
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
