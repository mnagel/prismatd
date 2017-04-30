package com.avona.games.towerdefence.tower.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.SlowDownParticle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyColliderPolicy;
import com.avona.games.towerdefence.tower.Tower;

public class SlowDownTower extends Tower {
	public SlowDownTower(final TimedCodeManager timedCodeManager, final int level) {
		super(timedCodeManager, new NearestEnemyColliderPolicy(), level);
		color = new RGB(1.0f, 1.0f, 1.0f);
		timer.time = 1.0f;
	}

	public SlowDownTower(final SlowDownTower other) {
		super(other);
	}

	@Override
	public String getName() {
		return "Slow-down tower";
	}

	@Override
	public Tower clone2() {
		return new SlowDownTower(this);
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new SlowDownParticle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				new RGB(1.0f, 1.0f, 1.0f),
				10
		);
	}

	@Override
	protected EnemySelectionPolicy getPolicyForLevel(int level) {
		return new NearestEnemyPolicy();
	}
}
