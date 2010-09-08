package com.avona.games.towerdefence;


public class Enemy extends MovingObject {
	protected World world;
	protected int health = 100;

	public Enemy(World world) {
		this.world = world;
		location = world.getInitialLocation();
		velocity = world.getDirection(location);
		System.out.println(velocity);
		System.out.println(location);
	}
	
	@Override
	public void step(double dt) {
		if(isDead())
			return;
		
		velocity.translate(location, dt);
		//System.out.println(velocity);
		//System.out.println(location);
	}

	public boolean isDead() {
		return health <= 0;
	}
	
	public void inflictDamage(int damage) {
		health -= damage;
		System.out.println(health);
	}
}
