package com.avona.games.towerdefence;

import java.util.List;
import java.util.Random;

import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class Particle extends MovingObject {
	private static final long serialVersionUID = 1L;

	public Enemy target;

	public int strengthR;
	public int strengthG;
	public int strengthB;
	protected float range = 5;
	protected double timeAlive = 4.0;

	protected boolean dead = false;
	protected double counter = 0.0;

	private ParticleCollidorPolicy collidorPolicy;

	public static Random r = new Random();

	public Particle(int level, V2 location, Enemy target,
			ParticleCollidorPolicy collidorPolicy) {
		this.location = new V2(location);
		this.target = target;
		this.collidorPolicy = collidorPolicy;

		this.velocity.setLength(150 + 2 * (level - 1));
		strengthR = strengthG = strengthB = 10 + 2 * (level - 1);
		recalculateTargetVector();
	}

	public void recalculateTargetVector() {
		velocity.setDirection(location, target.location);
	}

	public boolean collidedWith(Enemy e, final float dt) {
		return Collision.movingCircleCollidesWithCircle(location, velocity,
				range, e.location, e.velocity, e.radius, dt);
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

		e.inflictDamage(strengthR, strengthG, strengthB);
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void step(float dt) {
		if (target.isDead() || target.escaped)
			dead = true;

		if (isDead())
			return;

		recalculateTargetVector();
		location.addWeighted(velocity, dt);
	}

	public void collideWithEnemies(final List<Enemy> enemies, final float dt) {
		collidorPolicy.collideParticleWithEnemies(this, enemies, dt);
	}
}
