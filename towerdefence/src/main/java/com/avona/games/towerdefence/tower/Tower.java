package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.LocationObject;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.collidorPolicy.ParticleColliderPolicy;

import java.util.List;

public abstract class Tower extends LocationObject {
	private static final long serialVersionUID = 1L;

	public EnemySelectionPolicy enemySelectionPolicy;
	// TODO move to particle
	public ParticleColliderPolicy enemyParticleColliderPolicy;
	public int level;
	// The color the tower will be drawn with.
	public RGB color;
	protected RechargeTimer timer;
	protected int price;

	public Tower(TimedCodeManager timedCodeManager, ParticleColliderPolicy enemyParticleColliderPolicy, int level) {
		super(null, GridCell.size / 2);
		this.enemySelectionPolicy = getPolicyForLevel(level);
		this.enemyParticleColliderPolicy = enemyParticleColliderPolicy;
		this.level = level;
		timer = new RechargeTimer(timedCodeManager, 0.3f);
	}

	public Tower(final Tower t) {
		super(t);
		enemySelectionPolicy = t.enemySelectionPolicy;
		enemyParticleColliderPolicy = t.enemyParticleColliderPolicy;
		level = t.level;
		timer = t.timer.clone2();
		price = t.price;
		color = t.color;
	}

	abstract public String getName();

	public int getPrice() {
		return 10 + 2 * (level - 1);
	}

	public float getRange() {
		float base = 2.5f;
		float perLevel = 0.5f;
		return GridCell.size * (base + perLevel * (this.level - 1));
	}

	public int getLevelUpPrice() {
		return 5 + 2 * (level - 1);
	}

	public abstract Tower clone2();

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
	public void step(float dt) {
	}

	public Enemy findSuitableEnemy(List<Enemy> enemies) {
		return enemySelectionPolicy.findSuitableEnemy(this, enemies);
	}
}
