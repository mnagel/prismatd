package com.avona.games.towerdefence.tower.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.collidorPolicy.NearestEnemyColliderPolicy;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyWithColourPolicy;

public class GreenTower extends Tower {

	private static final long serialVersionUID = 730930808330710179L;

	public GreenTower(final TimedCodeManager timedCodeManager, final int level) {
		super(timedCodeManager, new NearestEnemyColliderPolicy(), level);
		color = new RGB(0, level * 10 + 10, 0);
	}

	public GreenTower(final GreenTower other) {
		super(other);
	}

	public String getName() {
		return "Emerald Prisma";
	}

	protected EnemySelectionPolicy getPolicyForLevel(int level) {
		if (level > 1) {
			return new NearestEnemyWithColourPolicy(false, true, false);
		}
		return new NearestEnemyPolicy();
	}

	@Override
	public Tower clone2() {
		return new GreenTower(this);
	}

	@Override
	public Particle makeParticle(final Enemy e) {
		return new Particle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				new RGB(
						0,
						10 + 2 * (level - 1),
						0
				)
		);
	}
}