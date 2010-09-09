package com.avona.games.towerdefence;

import javax.vecmath.Point2d;

public class Enemy extends MovingObject {
	protected World world;
	public int health = 100;

	public Enemy(World world, Point2d location) {
		this.world = world;
		this.location = location;
		velocity = world.getRandomDirection(location);
		System.out.println(velocity);
		System.out.println(location);
	}

	@Override
	public void step(double dt) {
		if (isDead())
			return;

		velocity.translate(location, dt);
		
		if (location.x < World.ORIGIN_X) {
			location.x = World.ORIGIN_X - (location.x - World.ORIGIN_X);
			velocity.direction.x = -velocity.direction.x;
		}
		if (location.y < World.ORIGIN_Y) {
			location.y = World.ORIGIN_Y - (location.y - World.ORIGIN_Y);
			velocity.direction.y = -velocity.direction.y;
		}
		if (location.x > World.WIDTH) {
			location.x = World.WIDTH - (location.x - World.WIDTH);
			velocity.direction.x = -velocity.direction.x;
		}
		if (location.y > World.HEIGHT) {
			location.y = World.HEIGHT - (location.y - World.HEIGHT);
			velocity.direction.y = -velocity.direction.y;
		}
		
		// System.out.println(velocity);
		// System.out.println(location);
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void inflictDamage(int damage) {
		health -= damage;
		System.out.println(health);
	}
}
