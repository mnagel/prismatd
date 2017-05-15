package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.collidorPolicy.ParticleColliderPolicy;

public class SlowDownParticle extends Particle {
	private int speedPenalty;

	public SlowDownParticle(V2 location, Enemy target, ParticleColliderPolicy colliderPolicy, int velocity, RGB strength, int speedPenalty) {
		super(location, target, colliderPolicy, velocity, strength);
		this.speedPenalty = speedPenalty;
	}

	@Override
	public void attack(Enemy e) {
		if (isDead()) {
			return;
		}

		float speed = e.getVelocity().length() - speedPenalty;
		speed = Math.max(speed, 0);
		e.getVelocity().setLength(speed);

		dead = true;
	}
}
