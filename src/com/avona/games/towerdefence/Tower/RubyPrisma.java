package com.avona.games.towerdefence.Tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.Enemy.Enemy;
import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.Particle.RubyParticle;

public class RubyPrisma extends Tower {

	private static final long serialVersionUID = 3932215952475151291L;

	@Override
	public Particle makeParticle(Enemy e) {
		return new RubyParticle(level, location, e,
				this.enemyParticleCollidorPolicy);
	}

	public RubyPrisma(final Tower t) {
		super(t);
		this.strength = new RGB(level * 10 + 10, 0, 0);
	}

	public Tower copy() {
		Tower x = new RubyPrisma(this);

		return x;
	}
}
