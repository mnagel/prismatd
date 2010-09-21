package com.avona.games.towerdefence;

public class Enemy extends MovingObject {
	public V2 target;
	public int health;
	public int maxHealth;
	public int level;
	public int waypointId = 1;
	public boolean escaped = false;
	public boolean left = false;
	public int worth;

	public Game game;

	public Enemy(World world, V2 location, int level, Game game) {
		this.level = level;
		this.maxHealth = 50 + 20 * level;
		this.health = this.maxHealth;
		this.worth = 25 + level;
		this.game = game;
		this.world = world;
		this.location = location;
		this.velocity.setLength(80 + 3 * level);
		setWPID(1);
	}

	public void setWPID(int i) {
		if (waypointId + 1 < world.waypoints.size()) {
			waypointId = i;
			target = world.waypoints.get(waypointId);
			velocity.setDirection(location, target);
		} else {
			escape();
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
		if (isDead()) {
			die();
		}
	}

	public void die() {
		game.money += worth;
	}

	public void escape() {
		escaped = true;
	}
}
