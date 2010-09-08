package com.avona.games.towerdefence;

import javax.vecmath.Point2d;

public class Enemy extends MovingObject {
	protected World world;
	protected int health = 100;

	public Enemy(World world, Point2d location) {
		this.world = world;
		this.location = location;
		velocity = world.getDirection(location);
		System.out.println(velocity);
		System.out.println(location);
	}

	@Override
	public void step(double dt) {
		if (isDead())
			return;

		velocity.translate(location, dt);
		
		double minx = -1;
		double miny = -1;
		
		double maxx = 1;
		double maxy = 1;
		
		if (location.x < minx) {
			location.x = minx - (location.x - minx);
			velocity.direction.x = -velocity.direction.x;
		}
		if (location.y < miny) {
			location.y = miny - (location.y - miny);
			velocity.direction.y = -velocity.direction.y;
		}
		if (location.x > maxx) {
			location.x = maxx - (location.x - maxx);
			velocity.direction.x = -velocity.direction.x;
		}
		if (location.y > maxy) {
			location.y = maxy - (location.y - maxy);
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
