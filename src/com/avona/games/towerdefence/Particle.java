package com.avona.games.towerdefence;

public class Particle extends MovingObject {
	protected int strength = 10;
	protected double range_sq = 5 * 5;
	protected double timeAlive = 4.0;

	protected boolean dead = false;
	protected Enemy target;
	protected double counter = 0.0;

	public Particle(V2 location, Enemy target) {
		this.location = new V2(location);
		this.target = target;

		velocity.speed = 80;

		recalculateTargetVector();
	}

	public void recalculateTargetVector() {
		velocity.fromto(location, target.location);
	}

	public boolean inRange(Enemy e) {
		return location.dist_sq(e.location) < range_sq;
	}

	/**
	 * Attack the enemy using a static damage value. The particle destroys
	 * itself after destroying an enemy. Further attack calls are then ignored.
	 * 
	 * @param e
	 *            Enemy to attack
	 */
	public void attack(Enemy e) {
		if (isDead()) {
			return;
		}

		e.inflictDamage(strength);
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void step(float dt) {
		if (target.isDead())
			dead = true;

		if (isDead())
			return;

		recalculateTargetVector();
		velocity.translate(location, dt);
	}
}
