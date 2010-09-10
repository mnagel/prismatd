package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class Enemy extends MovingObject {
	protected World world;
	public Vector2d target;
	public int health = 100;
	public int waypointid = 1;

	public Enemy(World world, Point2d location) {
		this.world = world;
		this.location = location;
		velocity = world.getRandomDirection(location);
		setWPID(1);
		System.out.println(velocity);
		System.out.println(location);
	}
	
	public void setWPID(int i) {
		Util.log("setting to wp" + 1);
		if (i == world.waypoints.size()) {
			i = 0;
		}
		waypointid = i;
		target = world.waypoints.get(waypointid);
		velocity.fromto(this.location, this.target);
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
