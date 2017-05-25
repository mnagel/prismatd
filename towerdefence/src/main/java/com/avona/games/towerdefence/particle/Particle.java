package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyEventListener;
import com.avona.games.towerdefence.engine.Collision;
import com.avona.games.towerdefence.engine.MovingObject;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.particle.collidorPolicy.ParticleColliderPolicy;

import java.util.List;
import java.util.Random;

public class Particle extends MovingObject {
	private static final long serialVersionUID = 1L;
	public static Random r = new Random();
	public Enemy target;
	public RGB strength;
	protected boolean dead = false;
	private ParticleColliderPolicy collidorPolicy;

	public Particle(V2 location, Enemy target, ParticleColliderPolicy collidorPolicy, int velocity, RGB strength) {
		super(location.clone2(), GridCell.size / 8);
		this.radius = 5;
		this.target = target;
		this.collidorPolicy = collidorPolicy;
		this.strength = strength;

		this.getVelocity().setLength(velocity);
		recalculateTargetVector();
	}

	public void recalculateTargetVector() {
		getVelocity().setDirection(location, target.location);
	}

	public boolean collidedWith(Enemy e, final float dt) {
		return Collision.movingCircleCollidesWithCircle(location, getVelocity(),
				0.5f * radius, e.location, e.getVelocity(), 0.1f * e.radius, dt);
	}

	/**
	 * Attack the enemy using a static damage value. The particle destroys
	 * itself after destroying an enemy. Further attack calls are then ignored.
	 *
	 * @param e Enemy to attack
	 */
	public void attack(Enemy e) {
		if (isDead()) {
			return;
		}

		e.inflictDamage(this.strength);
		dead = true;

		for (EnemyEventListener l : e.eventListeners) {
			l.onHurtEvent(e, this);
		}
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void step(float dt) {
		if (target.isDead() || target.escaped) {
			dead = true;
		}

		if (isDead()) {
			return;
		}

		recalculateTargetVector();
		location.addWeighted(getVelocity(), dt);
	}

	public void collideWithEnemies(final List<Enemy> enemies, final float dt) {
		collidorPolicy.collideParticleWithEnemies(this, enemies, dt);
	}
}
