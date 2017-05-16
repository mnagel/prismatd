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
	public RGB getDamage() {
		return new RGB(
				10 + 2 * (level - 1),
				0,
				0
		);
	}

	@Override
	public float getReloadTime() {
		return 0.3f;
	}

	@Override
	public Particle makeParticle(Enemy e) {
		return new Particle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				getDamage()
		);
	}
}
