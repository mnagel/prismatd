package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.EmeraldParticle;
import com.avona.games.towerdefence.particle.Particle;

public class EmeraldPrisma extends Tower {

	private static final long serialVersionUID = 730930808330710179L;

	@Override
	public Particle makeParticle(Enemy e) {
		return new EmeraldParticle(level, location, e,
				this.enemyParticleCollidorPolicy);
	}

	public EmeraldPrisma(final Tower t) {
		super(t);
		this.strength = new RGB(0, level * 10 + 10, 0);
	}

	public Tower copy() {
		Tower x = new EmeraldPrisma(this);

		return x;
	}
}
