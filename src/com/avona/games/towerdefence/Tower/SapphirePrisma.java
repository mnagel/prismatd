package com.avona.games.towerdefence.Tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.Particle.SapphireParticle;
import com.avona.games.towerdefence.enemy.Enemy;

public class SapphirePrisma extends Tower {

	private static final long serialVersionUID = -8299463941517744976L;

	@Override
	public Particle makeParticle(Enemy e) {
		return new SapphireParticle(level, location, e,
				this.enemyParticleCollidorPolicy);
	}

	public SapphirePrisma(final Tower t) {
		super(t);
		this.strength = new RGB(0, 0, level * 10 + 10);
	}

	public Tower copy() {
		Tower x = new SapphirePrisma(this);

		return x;
	}
}
