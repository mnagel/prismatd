package com.avona.games.towerdefence.Tower;

import com.avona.games.towerdefence.RechargeTimer;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class MousePointerTower extends Tower {

	private static final long serialVersionUID = 3512851716174082552L;

	@Override
	public Particle makeParticle(Enemy e) {
		return null;
	}

	public MousePointerTower(final Tower t) {
		super(t);
	}

	// FIXME one ctor should call the other
	public MousePointerTower(TimedCodeManager timedCodeManager,
			EnemySelectionPolicy enemySelectionPolicy,
			ParticleCollidorPolicy enemyParticleCollidorPolicy, int level) {
		super(timedCodeManager, enemySelectionPolicy,
				enemyParticleCollidorPolicy, level);
		this.enemySelectionPolicy = enemySelectionPolicy;
		this.enemyParticleCollidorPolicy = enemyParticleCollidorPolicy;
		this.level = level;
		timer = new RechargeTimer(timedCodeManager, 0.3f);
		range = 75 + 2 * (level - 1);
		price = 10 + 2 * (level - 1);
		radius = 16;
	}

	public Tower copy() {
		Tower x = new MousePointerTower(this);

		return x;
	}
}
