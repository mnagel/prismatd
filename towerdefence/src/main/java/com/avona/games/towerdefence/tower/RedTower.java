package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyWithColourPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyColliderPolicy;

public class RedTower extends Tower {

	private static final long serialVersionUID = 3932215952475151291L;

	public RedTower(final TimedCodeManager timedCodeManager, final int level) {
		super(timedCodeManager, new NearestEnemyColliderPolicy(), level);
		color = new RGB(level * 10 + 10, 0, 0);
	}

	public RedTower(final RedTower other) {
		super(other);
	}

	public String getName() {
		return "Ruby Prisma";
	}

	protected EnemySelectionPolicy getPolicyForLevel(int level) {
		if (level > 1) {
			return new NearestEnemyWithColourPolicy(true, false, false);
		}
		return new NearestEnemyPolicy();
	}

	@Override
	public Tower clone2() {
		return new RedTower(this);
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new Particle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				new RGB(
						10 + 2 * (level - 1),
						0,
						0
				)
		);
	}
}
