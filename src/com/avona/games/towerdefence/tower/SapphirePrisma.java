package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.SapphireParticle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyCollidorPolicy;

public class SapphirePrisma extends Tower {

	private static final long serialVersionUID = -8299463941517744976L;

	public SapphirePrisma(final TimedCodeManager timedCodeManager,
			final int level) {
		super(timedCodeManager, new NearestEnemyPolicy(),
				new NearestEnemyCollidorPolicy(), level);
		strength = new RGB(0, 0, level * 10 + 10);
	}

	public SapphirePrisma(final SapphirePrisma other) {
		super(other);
	}

	@Override
	public Tower copy() {
		return new SapphirePrisma(this);
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new SapphireParticle(level, location, e,
				enemyParticleCollidorPolicy);
	}
}
