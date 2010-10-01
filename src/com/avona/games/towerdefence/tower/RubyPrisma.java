package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.RubyParticle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyCollidorPolicy;

public class RubyPrisma extends Tower {

	private static final long serialVersionUID = 3932215952475151291L;

	public RubyPrisma(final TimedCodeManager timedCodeManager, final int level) {
		super(timedCodeManager, new NearestEnemyPolicy(),
				new NearestEnemyCollidorPolicy(), level);
		strength = new RGB(level * 10 + 10, 0, 0);
	}

	public RubyPrisma(final RubyPrisma other) {
		super(other);
	}

	@Override
	public Tower copy() {
		return new RubyPrisma(this);
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new RubyParticle(level, location, e, enemyParticleCollidorPolicy);
	}
}
