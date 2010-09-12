package com.avona.games.towerdefence;

public class Enemy extends MovingObject {
	public V2 target;
	public int health = 100;
	public int waypointId = 1;
	public boolean escaped = false;
	public boolean left = false;

	public Enemy(World world, V2 location) {
		this.world = world;
		this.location = location;
		this.velocity.setLength(80);
		setWPID(1);
	}

	public void setWPID(int i) {
		if (waypointId + 1 < world.waypoints.size()) {
			waypointId = i;
			target = world.waypoints.get(waypointId);
			velocity.setDirection(location, target);
		} else {
			escaped = true;
		}
	}

	@Override
	public void step(float dt) {
		if (isDead())
			return;

		location.addWeighted(velocity, dt);
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void inflictDamage(int damage) {
		health -= damage;
	}
}
