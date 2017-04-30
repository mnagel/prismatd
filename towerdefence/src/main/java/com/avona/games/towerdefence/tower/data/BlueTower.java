package com.avona.games.towerdefence.tower.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyWithColourPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyColliderPolicy;
import com.avona.games.towerdefence.tower.Tower;

public class BlueTower extends Tower {

	private static final long serialVersionUID = -8299463941517744976L;

	public BlueTower(final TimedCodeManager timedCodeManager, int level) {
		super(timedCodeManager, new NearestEnemyColliderPolicy(), level);
		color = new RGB(0, 0, level * 10 + 10);
	}

	public BlueTower(final BlueTower other) {
		super(other);
	}

	public String getName() {
		return "Sapphire Prisma";
	}

	protected EnemySelectionPolicy getPolicyForLevel(int level) {
		if (level > 1) {
			return new NearestEnemyWithColourPolicy(false, false, true);
		}
		return new NearestEnemyPolicy();
	}

	@Override
	public Tower clone2() {
		return new BlueTower(this);
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new Particle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				new RGB(
						0,
						0,
						10 + 2 * (level - 1)
				)
		);
	}
}
