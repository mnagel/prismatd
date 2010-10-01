package com.avona.games.towerdefence.particle;

import java.util.List;
import java.util.Random;

import com.avona.games.towerdefence.Collision;
import com.avona.games.towerdefence.MovingObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class Particle extends MovingObject {
	private static final long serialVersionUID = 1L;

	public Enemy target;

	public RGB strength;

	protected float range = 5;
	protected double timeAlive = 4.0;

	protected boolean dead = false;
	protected double counter = 0.0;

	private ParticleCollidorPolicy collidorPolicy;

	public static Random r = new Random();

	public Particle(V2 location, Enemy target,
			ParticleCollidorPolicy collidorPolicy, int velocity, RGB strength) {
		this.location = new V2(location);
		this.target = target;
		this.collidorPolicy = collidorPolicy;
		this.strength = strength;

		this.velocity.setLength(velocity);
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

		e.inflictDamage(this.strength);
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
