package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.particle.EmeraldParticle;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyCollidorPolicy;

public class EmeraldPrisma extends Tower {

	private static final long serialVersionUID = 730930808330710179L;

	public EmeraldPrisma(final TimedCodeManager timedCodeManager,
			final int level) {
		super(timedCodeManager, new NearestEnemyPolicy(),
				new NearestEnemyCollidorPolicy(), level);
		strength = new RGB(0, level * 10 + 10, 0);
	}

	public EmeraldPrisma(final EmeraldPrisma other) {
		super(other);
	}

	@Override
	public Tower copy() {
		return new EmeraldPrisma(this);
	}

	@Override
	public Particle makeParticle(final Enemy e) {
		return new EmeraldParticle(level, location, e,
				enemyParticleCollidorPolicy);
	}
}
