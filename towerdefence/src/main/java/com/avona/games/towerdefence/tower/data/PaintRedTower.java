package com.avona.games.towerdefence.tower.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.PaintballParticle;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.collidorPolicy.NearestEnemyColliderPolicy;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.tower.enemySelection.NearestEnemyWithColourPolicy;

public class PaintRedTower extends Tower {
	private RGB particleMask;

	public PaintRedTower(final int level) {
		super(new NearestEnemyColliderPolicy(), level);
		color = new RGB(0, 1, 1);
		particleMask = new RGB(1.0f, 0, 0);
	}

	public PaintRedTower(final PaintRedTower other) {
		super(other);
		this.particleMask = other.particleMask;
	}

	public String getName() {
		return "Paint Red Prisma";
	}

	protected EnemySelectionPolicy getPolicyForLevel(int level) {
		if (level > 1) {
			return new NearestEnemyWithColourPolicy(false, true, true);
		}
		return new NearestEnemyPolicy();
	}

	@Override
	public void upgrade() {
		super.upgrade();
		this.enemySelectionPolicy = getPolicyForLevel(level);
	}

	@Override
	public Tower clone2() {
		return new PaintRedTower(this);
	}

	@Override
	public RGB getDamage() {
		return new RGB(
				0,
				10 * level,
				0
		);
	}

	@Override
	public float getReloadTime() {
		return 0.3f;
	}

	@Override
	public Particle makeParticle(final Enemy e) {
		return new PaintballParticle(
				location,
				e,
				enemyParticleColliderPolicy,
				150 + 2 * (level - 1),
				getDamage(),
				particleMask
		);
	}
}
