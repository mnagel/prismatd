package com.avona.games.towerdefence.tower.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.collidorPolicy.NearestEnemyColliderPolicy;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyWithColourPolicy;

public class BlueTower extends Tower {

	private static final long serialVersionUID = -8299463941517744976L;

	public BlueTower(int level) {
		super(new NearestEnemyColliderPolicy(), level);
		color = new RGB(0, 0, 1);
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
	public RGB getDamage() {
		return new RGB(
				0,
				0,
				11 * level * level
		);
	}

	@Override
	public float getReloadTime() {
		return 0.3f * level;
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
