package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.engine.LocationObject;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.collidorPolicy.ParticleColliderPolicy;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;

import java.util.List;

public abstract class Tower extends LocationObject {
	private static final long serialVersionUID = 1L;

	public RGB color; // gfx

	public int level;

	public EnemySelectionPolicy enemySelectionPolicy;
	// TODO move to particle
	protected ParticleColliderPolicy enemyParticleColliderPolicy;

	private RechargeTimer timer;

	public Tower(ParticleColliderPolicy enemyParticleColliderPolicy, int level) {
		super(null, GridCell.size / 2);
		this.enemySelectionPolicy = getPolicyForLevel(level);
		this.enemyParticleColliderPolicy = enemyParticleColliderPolicy;
		this.level = level;

	}

	public Tower(final Tower t) {
		super(t);
		enemySelectionPolicy = t.enemySelectionPolicy;
		enemyParticleColliderPolicy = t.enemyParticleColliderPolicy;
		level = t.level;
		if (t.timer != null) {
			timer = t.timer.clone2();
		}
		color = t.color;
	}

	public void registerAtTimedCodeManager(TimedCodeManager tcm) {
		timer = new RechargeTimer(tcm, getReloadTime());
	}

	abstract public String getName();

	// cumulative (incl. all upgrades)
	private int getPrice(int lvl) {
		return 10 * lvl * lvl;
	}

	// cumulative (incl. all upgrades)
	public int getPrice() {
		return getPrice(level);
	}

	// cumulative (incl. all upgrades)
	public int getUpgradePrice() {
		return getPrice(level + 1) - getPrice(level);
	}

	public float getRange() {
		float base = 2.5f;
		float perLevel = 0.5f;
		return GridCell.size * (base + perLevel * (this.level - 1));
	}

	public abstract Tower clone2();

	public abstract RGB getDamage();

	public abstract float getReloadTime();

	public abstract Particle makeParticle(Enemy e);

	public void setLevel(int level) {
		this.level = level;
		this.enemySelectionPolicy = getPolicyForLevel(level);
	}

	protected abstract EnemySelectionPolicy getPolicyForLevel(int level);

	public Particle shootTowards(Enemy e) {
		if (timer.ready) {
			timer.rearm();
			return makeParticle(e);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.getName() + " Level " + level;
	}

	@Override
	public void step(float dt) {
	}

	public Enemy findSuitableEnemy(List<Enemy> enemies) {
		return enemySelectionPolicy.findSuitableEnemy(this, enemies);
	}
}
